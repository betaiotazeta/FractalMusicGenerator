package org.betaiotazeta.fractalmusicgenerator;

import com.aparapi.Range;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class RenderManager {

    public RenderManager(FmgApp fmgApp) {
        this.fmgApp = fmgApp;
    }

    public void render(FractalPanel fractalPanel, int[] colorsArray, int maxImageIterations,
            int width, int height, double minA, double maxA, double minB, double maxB) throws InterruptedException {
        BufferedImage imageFractal = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        final int[] imageArray = ((DataBufferInt) imageFractal.getRaster().getDataBuffer()).getData();
        double[][] iterCountArray = new double[width][height];
        int[] iterCountFreqArray = new int[maxImageIterations + 1];
        int fractalIndex = fmgApp.getConfigurator().getFractalIndex();
        int smooth = 0;
        if (fmgApp.getSmoothCheckBox().isSelected()) {
            smooth = 1;
        }
        int histogram = 0;
        if (fmgApp.getHistogramCheckBox().isSelected()) {
            histogram = 1;
        }
        int bailout = 1000;
        if (smooth == 1 && histogram == 1) {
            bailout = 4;
        }
        // using Device is an optional way to customize Aparapi
//        Device device = fmgApp.getDevice();
        AKernel kernel = fmgApp.getKernel();
        // iterCountFreqArray needs to be sychronized, not supported in Aparapi, no need to pass it in 
        kernel.setupKernel(iterCountArray, imageArray, colorsArray, maxImageIterations,
                width, height, minA, maxA, minB, maxB, fractalIndex, smooth, histogram, bailout);
//        Range range = device.createRange(width * height);
//        kernel.execute(range);
        kernel.execute(Range.create(width * height));

        if (histogram == 1) {
            // HISTOGRAM
            // *) iterCountArray:
            // bidimensional, contains iteration count for every pixel
            // *) iterCountFreqArray:
            // index: amount of iterations performed
            // value: quantity of pixels escaping with index amount of iterations
            // *) percentCountArray;
            // index: amount of iterations performed
            // value: percentage of pixels escaped with the amount of iterations performed
            // *) percentageArray[i][j]: NOT USED
            // value: percentage of pixels escaped with the amount of iterations performed for the pixel
            // range 0 - 1

            // we can avoid synchronization in AKernel if we do this here
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    int index = (int) iterCountArray[i][j];
                    iterCountFreqArray[index]++;
                }
            }

            double total = 0d; // would be width*height if inSet pixels were included (maxImagIter +1)
            for (int i = 0; i < maxImageIterations; i++) {
                total = total + iterCountFreqArray[i];
            }

            // NOT USED
//            double[][] percentageArray = new double[width][height];
//            for (int i = 0; i < width; i++) {
//                for (int j = 0; j < height; j++) {
//                    int iterCount = (int) iterCountArray[i][j];
//                    // total percentage up to iterCount
//                    for (int k = 0; k <= iterCount; k++) {
//                        // must be floating-point division
//                        percentageArray[i][j] += iterCountFreqArray[k] / total;
//                    }
//                }
//            }

            double[] percentCountArray = new double[maxImageIterations + 1];
            percentCountArray[0] = (iterCountFreqArray[0] / total);
            for (int i = 1; i < maxImageIterations; i++) {
                percentCountArray[i] = percentCountArray[i - 1] + (iterCountFreqArray[i] / total);
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

            int index = 0;
            for (int j = 0; j < height; j++) {
                for (int i = 0; i < width; i++) {
                    // interpolation
                    double countD = iterCountArray[i][j];
                    if (countD < maxImageIterations) {
                        double fraction = countD % 1; // fractional part

                        int color1 = colorsArray[(int) percentCountArray[(int) countD]];
                        int color2 = colorsArray[(int) percentCountArray[(int) countD + 1]];

                        // color3 = linearInterpolate(color1, color2, fraction)
                        int red1 = (color1 >> 16) & 0xFF;
                        int green1 = (color1 >> 8) & 0xFF;
                        int blue1 = color1 & 0xFF;
                        int red2 = (color2 >> 16) & 0xFF;
                        int green2 = (color2 >> 8) & 0xFF;
                        int blue2 = color2 & 0xFF;

                        int red3 = (int) ((red2 - red1) * fraction + red1);
                        int green3 = (int) ((green2 - green1) * fraction + green1);
                        int blue3 = (int) ((blue2 - blue1) * fraction + blue1);

                        int color3 = red3;
                        color3 = (color3 << 8) + green3;
                        color3 = (color3 << 8) + blue3;

                        imageArray[index] = color3;
                    } else {
                        imageArray[index] = colorsArray[100]; // 100 palette length
                    }
                    index++;
                }
            }
        }

        fractalPanel.setImageFractal(imageFractal);
        fmgApp.getFractalPanel().repaint();
    }

    // Custom variables
    private FmgApp fmgApp;
}
