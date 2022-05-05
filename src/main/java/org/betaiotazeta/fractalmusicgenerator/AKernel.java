package org.betaiotazeta.fractalmusicgenerator;

import com.aparapi.Kernel;

public class AKernel extends Kernel {

    @Override
    public void run() {

        int gid = getGlobalId();
        // width * height = max gid
        // in an image values are stored line after line from top to bottom
        final int px = (gid % width);
        final int py = (gid / width);

        double zx = ((maxA - minA) * px / width) + minA;
        double zy = ((minB - maxB) * py / height) + maxB;

        double countD = 0;
        if (fractalIndex == 0) {
            countD = calculateMandelbrotIterations(zx, zy);
        } else if (fractalIndex == 1) {
            countD = calculateBurningShipIterations(zx, zy);
        } else if (fractalIndex == 2) {
            countD = calculateTricornIterations(zx, zy);
        } else if (fractalIndex == 3) {
            countD = calculateRectangleIterations(zx, zy);
        } else if (fractalIndex == 4) {
            countD = calculateKlingonIterations(zx, zy);
        } else if (fractalIndex == 5) {
            countD = calculateCrownIterations(zx, zy);
        } else if (fractalIndex == 6) {
            countD = calculateFrogIterations(zx, zy);
        } else if (fractalIndex == 7) {
            countD = calculateMandelshipIterations(zx, zy);
        } else if (fractalIndex == 8) {
            countD = calculateFrankensteinIterations(zx, zy);
        } else if (fractalIndex == 9) {
            countD = calculateLogisticIterations(zx, zy);
        }
        
        // 100 is palette length (colorsArray.legth)
        // higher iteration counts will cycle unless histogram is used
        
        // SMOOTHING
        if (smooth == 1) {
            if (countD == maxImageIterations) {
                imageArray[gid] = colorsArray[100];
            } else {
                double fraction = countD - Math.floor(countD); // fractional part
                int count = ((int) countD) % 99; // cycling colors

                int color1 = colorsArray[count];
                int color2 = colorsArray[count + 1];

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

                imageArray[gid] = color3;
            }
            return;
        }
        
        // HISTOGRAM + SMOOTHING
        if (histogram == 1) {
            iterCountArray[px][py] = countD;
            // synchronization is not supported on Aparapi, will do this in RenderManager
            // synchronized (monitor) {
            //    iterCountFreqArray[(int) countD]++;
            // }
            return;
        }
        
        // NO histogram, NO smoothing
        // simply put the value from the precalculated palette for this iteration count (% adjusted)
        if (countD == maxImageIterations) {
            imageArray[gid] = colorsArray[100];
        } else {
            int count = ((int) countD) % 100;
            imageArray[gid] = colorsArray[count];
        }
    }

    public double calculateMandelbrotIterations(double zx, double zy) {
        int count = 0;
        double cx = zx;
        double cy = zy;
        double zxNext;

        // iterate until Z orbit diverges too far or until maxImageIterations is reached
        while ((count < maxImageIterations) && (((zx * zx) + (zy * zy)) < bailout)) {
            zxNext = ((zx * zx) - (zy * zy)) + cx;
            zy = 2 * zx * zy + cy;
            zx = zxNext;
            count++;
        }

        if (bailout == 4) {
            return count;
        }
        
        if (count < maxImageIterations) {
            // Wikipedia:
            // Rearranging the potential function.
            // Dividing log_zn by log(2) instead of log(N = 1<<8)
            // because we want the entire palette to range from the
            // center to radius 2, NOT our bailout radius.
//            double log_zn = Math.log(zx * zx + zy * zy) / 2;
//            double nu = Math.log(log_zn / Math.log(2)) / Math.log(2);
//            countD = count + 1 - nu; // nu < 1.3, nu > 0
            
            // http://www.fractalforums.com/programming/please-explain-smooth-coloring/
            // c = x + yi
            // magnitude = sqrt(x^2+y^2)
            // magnitude = cabs = abs
            // |x + yi| = abs(x + yi) = cabs(x + yi) = sqrt(x^2 + y^2)
            // 0.5 * log(value) = log(sqrt(value)) // sqrt of inner term removed using log simplification rules
            
            // If you want to use the minimum bailout (i.e. testing the squared magnitude against 4)
            // then to get smooth results you need to estimate "p" instead of using the fixed constant:
//            double zxOld, zyOld;
//            double p = Math.log((zx * zx) + (zy * zy)) / Math.log((zxOld * zxOld) + (zyOld * zyOld));
//            double fraction = (Math.log(0.5 * Math.log((zx * zx) + (zy * zy))) - Math.log(0.5 * Math.log(4))) / Math.log(p);

            double fraction = (Math.log(0.5 * Math.log((zx * zx) + (zy * zy))) - CON1) / CON2;
//            countD = count + 1 - (Math.log(0.5 * Math.log((zx * zx) + (zy * zy))) - CON1) / CON2;
            
            // this is used to prevent countD to be negative
            // is (color density double) involved?
            if (fraction > 1) { // to avoid histogram glitches when zoomed far out and sideways
                fraction = 1; // fraction always > 0; occasionally may be slightly bigger than 1
            }
            return count + 1 - fraction;
        } else {
            return count; // maxImageIterations
        } 
    }    
    
    public double calculateBurningShipIterations(double zx, double zy) {
        int count = 0;
        double cx = zx;
        double cy = zy;
        double zxNext;

        while ((count < maxImageIterations) && (((zx * zx) + (zy * zy)) < bailout)) {
            zxNext = ((zx * zx) - (zy * zy)) + cx;
            zy = Math.abs(2 * zx * zy) + cy;
            zx = zxNext;
            count++;
        }       
        if (bailout == 4) {
            return count;
        }       
        if (count < maxImageIterations) {
            double fraction = (Math.log(0.5 * Math.log((zx * zx) + (zy * zy))) - CON1) / CON2;
            if (fraction > 1) {
                fraction = 1;
            }
            return count + 1 - fraction;
        } else {
            return count;
        }
    }
    
    public double calculateTricornIterations(double zx, double zy) {
        int count = 0;
        double cx = zx;
        double cy = zy;
        double zxNext;

        while ((count < maxImageIterations) && (((zx * zx) + (zy * zy)) < bailout)) {
            // Zn+1 = conj(Z*Z) + C // main formula
            zxNext = zx * zx - zy * zy + cx;
            zy = -2 * zx * zy + cy;
            zx = zxNext;
            count++;
        }
        if (bailout == 4) {
            return count;
        }
        if (count < maxImageIterations) {
            double fraction = (Math.log(0.5 * Math.log((zx * zx) + (zy * zy))) - CON1) / CON2;
            if (fraction > 1) {
                fraction = 1;
            }
            return count + 1 - fraction;           
        } else {
            return count;
        }
    }
    
    public double calculateRectangleIterations(double zx, double zy) {
        int count = 0;
        double cx = zx;
        double cy = zy;
        double zxNext;

        while ((count < maxImageIterations) && (((zx * zx) + (zy * zy)) < bailout)) {
            //  Zn = (zx + zy*i)     C = (cx + cy*i)     C2 = (cx*cx + cy*cy*i)
            // Zn+1 = Zn * (zx*zx + zy*zy) - (Zn * C2) // main formula
            // a real component of Z, b imaginary component of Z
            // c real component of C, d imaginary component of C
            // (a+bi)*(a*a+b*b)-((a+bi)*(c*c+d*d*i))
            // (a^3+ab^2-ac^2+bd^2)+(a^2b+b^3-ad^2-bc^2)i // expressed in the form of (a + bi)
            zxNext = zx * zx * zx + zx * zy * zy - zx * cx * cx + zy * cy * cy;
            zy = zx * zx * zy + zy * zy * zy - zx * cy * cy - zy * cx * cx;
            zx = zxNext;
            count++;
        }        
        if (bailout == 4) {
            return count;
        }
        if (count < maxImageIterations) {
            double fraction = (Math.log(0.5 * Math.log((zx * zx) + (zy * zy))) - CON1) / CON3;
            if (fraction > 1) {
                fraction = 1;
            }
            return count + 1 - fraction;
        } else {
            return count;
        }
    }
    
    public double calculateKlingonIterations(double zx, double zy) {
        int count = 0;
        double cx = zx;
        double cy = zy;
        double zxNext;

        while ((count < maxImageIterations) && (((zx * zx) + (zy * zy)) < bailout)) {
            zxNext = Math.abs(zx * zx * zx) - 3 * zy * zy * Math.abs(zx) + cx;
            zy = 3 * zx * zx * Math.abs(zy) - Math.abs(zy * zy * zy) + cy;
            zx = zxNext;
            count++;
        }       
        if (bailout == 4) {
            return count;
        }       
        if (count < maxImageIterations) {
            double fraction = (Math.log(0.5 * Math.log((zx * zx) + (zy * zy))) - CON1) / CON3;
            if (fraction > 1) {
                fraction = 1;
            }
            return count + 1 - fraction;
        } else {
            return count;
        }
    }    

        public double calculateCrownIterations(double zx, double zy) {
        int count = 0;
        double cx = zx;
        double cy = zy;
        double zxNext;

        while ((count < maxImageIterations) && (((zx * zx) + (zy * zy)) < bailout)) {
            zxNext = zx * zx * zx - 3 * zx * zy * zy + cx;
            zy = Math.abs(3 * zx * zx * zy - zy * zy * zy) + cy;
            zx = zxNext;
            count++;
        }       
        if (bailout == 4) {
            return count;
        }       
        if (count < maxImageIterations) {
            double fraction = (Math.log(0.5 * Math.log((zx * zx) + (zy * zy))) - CON1) / CON3;
            if (fraction > 1) {
                fraction = 1;
            }
            return count + 1 - fraction;
        } else {
            return count;
        }
    }

        public double calculateFrogIterations(double zx, double zy) {
        int count = 0;
        double cx = zx;
        double cy = zy;
        double zxNext;

        while ((count < maxImageIterations) && (((zx * zx) + (zy * zy)) < bailout)) {
            zxNext = Math.abs(zx * zx * zx - 3 * zx * zy * zy) + cx;
            zy = Math.abs(3 * zx * zx * zy - zy * zy * zy) + cy;
            zx = zxNext;
            count++;
        }       
        if (bailout == 4) {
            return count;
        }       
        if (count < maxImageIterations) {
            double fraction = (Math.log(0.5 * Math.log((zx * zx) + (zy * zy))) - CON1) / CON3;
            if (fraction > 1) {
                fraction = 1;
            }
            return count + 1 - fraction;
        } else {
            return count;
        }
    }

        public double calculateMandelshipIterations(double zx, double zy) {
        int count = 0;
        double cx = zx;
        double cy = zy;
        double zxNext;

        while ((count < maxImageIterations) && (((zx * zx) + (zy * zy)) < bailout)) {
            zxNext = -2*cx*zy*zy - 6*zx*zx*zy*zy + zy*zy*zy*zy - 4*cy*zx*zy + cx*cx - cy*cy + zx*zx*zx*zx + 2*cx*zx*zx + cx;
            zy = -2*cy*zy*zy - 4*zx*zy*zy*zy + 4*zx*zx*zx*zy + 4*cx*zx*zy + 2*cx*cy + 2*cy*cx*cx + cy;
            zx = zxNext;
            count++;
        }       
        if (bailout == 4) {
            return count;
        }       
        if (count < maxImageIterations) {
            double fraction = (Math.log(0.5 * Math.log((zx * zx) + (zy * zy))) - CON1) / CON4;
            if (fraction > 1) {
                fraction = 1;
            }
            return count + 1 - fraction;
        } else {
            return count;
        }
    }

        public double calculateFrankensteinIterations(double zx, double zy) {
        int count = 0;
        double cx = zx;
        double cy = zy;
        double zxNext;

        while ((count < maxImageIterations) && (((zx * zx) + (zy * zy)) < bailout)) {
            zxNext = Math.tanh(zx * zx * zx - 3 * zx * zy * zy) + cx;
            zy = Math.abs(3 * zx * zx * zy - zy * zy * zy) + cy;
            zx = zxNext;
            count++;
        }       
        if (bailout == 4) {
            return count;
        }       
        if (count < maxImageIterations) {
            double fraction = (Math.log(0.5 * Math.log((zx * zx) + (zy * zy))) - CON1) / CON3;
            if (fraction > 1) {
                fraction = 1;
            }
            return count + 1 - fraction;
        } else {
            return count;
        }
    }

        public double calculateLogisticIterations(double zx, double zy) {
        int count = 0;
        double cx = zx;
        double cy = zy;
        double zxNext;

        while ((count < maxImageIterations) && (((zx * zx) + (zy * zy)) < bailout)) {
            zxNext = -cx*zx*zx + cx*zx + 2*cy*zx*zy + cx*zy*zy - cy*zy;
            zy = cx*zy + cy*zx - cy*zx*zx + cy*zy*zy - 2*cx*zx*zy;
            zx = zxNext;
            count++;
        }       
        if (bailout == 4) {
            return count;
        }       
        if (count < maxImageIterations) {
            double fraction = (Math.log(0.5 * Math.log((zx * zx) + (zy * zy))) - CON1) / CON2;
            if (fraction > 1) {
                fraction = 1;
            }
            return count + 1 - fraction;
        } else {
            return count;
        }
    }        

    public void setupKernel(double[][] iterCountArray, int[] imageArray, int[] colorsArray, int maxImageIterations,
            int width, int height, double minA, double maxA, double minB, double maxB, int fractalIndex, int smooth, int histogram, int bailout) {
        this.iterCountArray = iterCountArray;
//        this.iterCountFreqArray = iterCountFreqArray;
        this.imageArray = imageArray;
        this.colorsArray = colorsArray;
        this.maxImageIterations = maxImageIterations;
        this.width = width;
        this.height = height;
        this.minA = minA;
        this.maxA = maxA;
        this.minB = minB;
        this.maxB = maxB;
        this.fractalIndex = fractalIndex;
        this.smooth = smooth;
        this.histogram = histogram;
        this.bailout = bailout;
    }

    // Variables
    private int[] imageArray;
    private int[] colorsArray;
    private int maxImageIterations;
    private int width;
    private int height;
    private double minA;
    private double maxA;
    private double minB;
    private double maxB;
    private int fractalIndex;
    private double[][] iterCountArray;
//    private int[] iterCountFreqArray;
    private int smooth;
    private int histogram;
    private int bailout;
//    private Object monitor = new Object();
    private final double CON1 = 1.23949755335612; // Math.log(0.5 * Math.log(1000))
    private final double CON2 = 0.6931471805599453; // Math.log(2)
    private final double CON3 = 1.0986122886681098; // Math.log(3)
    private final double CON4 = 1.3862943611198906; // Math.log(4)
//    private final double CON5 = 1.6094379124341003; // Math.log(5)
}
