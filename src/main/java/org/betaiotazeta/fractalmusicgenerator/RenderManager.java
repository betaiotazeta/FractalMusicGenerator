package org.betaiotazeta.fractalmusicgenerator;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;

public class RenderManager {

    public RenderManager(FmgApp fmgApp) {
        this.fmgApp = fmgApp;
        this.kernel = fmgApp.getKernel();
        this.renderExecutorService = fmgApp.getRenderExecutorService();
        ThreadPoolExecutor executor = (ThreadPoolExecutor) renderExecutorService;
        this.workerCount = executor.getCorePoolSize();
    }

    public void render(FractalPanel fractalPanel, int[] colorsArray, int maxImageIterations,
            int width, int height, double minA, double maxA, double minB, double maxB) throws InterruptedException {
        BufferedImage imageFractal = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        final int[] imageArray = ((DataBufferInt) imageFractal.getRaster().getDataBuffer()).getData();
        float[] iterCountArray = new float[width * height];
        int fractalIndex = fmgApp.getConfigurator().getFractalIndex();
        int smooth = fmgApp.getSmoothCheckBox().isSelected() ? 1 : 0;
        int histogram = fmgApp.getHistogramCheckBox().isSelected() ? 1 : 0;
        int bailout = (smooth == 0 && histogram == 0) ? 4 : 1000;

        LongAdder[] iterCountFreqArray = null;
        if (histogram == 1) {
            iterCountFreqArray = new LongAdder[maxImageIterations + 1];
            for (int i = 0; i < iterCountFreqArray.length; i++) {
                iterCountFreqArray[i] = new LongAdder();
            }
        }

        kernel.setupKernel(iterCountArray, iterCountFreqArray, imageArray, colorsArray, maxImageIterations,
                width, height, minA, maxA, minB, maxB, fractalIndex, smooth, histogram, bailout);

        final int totalPixels = width * height;
        final AtomicInteger nextIndex = new AtomicInteger(0);
        final int chunkSize = 256;
        final CountDownLatch doneSignal = new CountDownLatch(workerCount);

        for (int idx = 0; idx < workerCount; idx++) {
            renderExecutorService.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        int start;
                        while ((start = nextIndex.getAndAdd(chunkSize)) < totalPixels) {
                            int end = start + chunkSize;
                            if (end > totalPixels) {
                                end = totalPixels;
                            }
                            for (int gid = start; gid < end; gid++) {
                                kernel.renderPixel(gid);
                            }
                        }
                    } finally {
                        doneSignal.countDown();
                    }
                }
            });
        }

        doneSignal.await();

        // HISTOGRAM
        if (histogram == 1) {
            // *) iterCountArray:
            // contains iteration count for every pixel
            // *) iterCountFreqArray:
            // index: amount of iterations performed
            // value: quantity of pixels escaping with index amount of iterations
            // *) percentCountArray;
            // index: amount of iterations performed
            // value: percentage of pixels escaped with the amount of iterations performed
            // range 0 - 1
            double total = 0d; // would be width*height if inSet pixels were included (maxImageIterations +1)

            for (int i = 0; i < maxImageIterations; i++) {
                total = total + iterCountFreqArray[i].sum();
            }

            if (total <= 0d) {
                total = 1d;
            }

            double[] percentCountArray = new double[maxImageIterations + 1];
            percentCountArray[0] = iterCountFreqArray[0].sum() / total;
            for (int i = 1; i < maxImageIterations; i++) {
                percentCountArray[i] = percentCountArray[i - 1] + (iterCountFreqArray[i].sum() / total);
                if (percentCountArray[i] >= 1) {
                    percentCountArray[i] = 0.999999999999; // 1 means inSet
                }
            }

            // shifting values to valorize high iteration points with more colors
            // f(x) = 1 - (1 - x)^(1 / 2)
            // multiplying with maxImageIterations to have that range
            double density = (double) fmgApp.getPaletteDensitySpinner().getValue(); // 2
            for (int i = 0; i < maxImageIterations; i++) {
                percentCountArray[i] = (1.0 - Math.pow(1.0 - percentCountArray[i], 1.0 / density)) * 100; // 100 palette length
            }

            // copy the last bucket: fall back to the last valid entry
            percentCountArray[maxImageIterations] = percentCountArray[maxImageIterations - 1];

            for (int gid = 0; gid < totalPixels; gid++) {
                float countF = iterCountArray[gid];
                if (countF < maxImageIterations) {
                    double fraction = countF % 1;
                    int bucket = (int) countF;
                    int color1 = colorsArray[(int) percentCountArray[bucket]];
                    int color2 = colorsArray[(int) percentCountArray[bucket + 1]];
                    imageArray[gid] = FractalKernel.interpolateColor(color1, color2, fraction);
                } else {
                    imageArray[gid] = colorsArray[100]; // 100 palette length
                }
            }
        }

        fractalPanel.setImageFractal(imageFractal);
        fractalPanel.repaint();
    }

    // Custom variables
    private final FmgApp fmgApp;
    private final FractalKernel kernel;
    private final ExecutorService renderExecutorService;
    private final int workerCount;
}
