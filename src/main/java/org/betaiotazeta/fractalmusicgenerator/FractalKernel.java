package org.betaiotazeta.fractalmusicgenerator;

import java.util.concurrent.atomic.LongAdder;

public class FractalKernel {

    public void renderPixel(int gid) {
        // width * height = max gid
        // in an image values are stored line after line from top to bottom
        final int px = gid % width;
        final int py = gid / width;

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

        // 100 is palette length (colorsArray.length)
        // higher iteration counts will cycle unless histogram is used
        
        // SMOOTHING
        if (smooth == 1) {
            if (countD == maxImageIterations) {
                imageArray[gid] = colorsArray[100];
            } else {
                double fraction = countD - Math.floor(countD); // fractional part
                int count = ((int) countD) % 99; // cycling colors
                imageArray[gid] = interpolateColor(colorsArray[count], colorsArray[count + 1], fraction);
            }
            return;
        }

        // HISTOGRAM
        if (histogram == 1) {
            iterCountArray[gid] = (float) countD;
            int bucket = (int) countD;
            if (bucket > maxImageIterations) {
                bucket = maxImageIterations;
            }
            iterCountFreqArray[bucket].increment();
            return;
        }

        // NO histogram, NO smoothing
        // simply put the value from the precalculated palette for this iteration count (% adjusted)
        if (countD == maxImageIterations) {
            imageArray[gid] = colorsArray[100];
        } else {
            imageArray[gid] = colorsArray[((int) countD) % 100];
        }
    }

    public static int interpolateColor(int color1, int color2, double fraction) {
        // linear interpolation
        int red1 = (color1 >> 16) & 0xFF;
        int green1 = (color1 >> 8) & 0xFF;
        int blue1 = color1 & 0xFF;

        int red2 = (color2 >> 16) & 0xFF;
        int green2 = (color2 >> 8) & 0xFF;
        int blue2 = color2 & 0xFF;

        int red3 = (int) ((red2 - red1) * fraction + red1);
        int green3 = (int) ((green2 - green1) * fraction + green1);
        int blue3 = (int) ((blue2 - blue1) * fraction + blue1);

        return (red3 << 16) | (green3 << 8) | blue3;
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
            // Why the double logarithm appears:
            // Outside the Mandelbrot set: |z(n+1)| ≈ |z(n)|²
            // Taking logs: log |z(n+1)| ≈ 2 log |z(n)|
            // Taking logs again: log(log|z(n+1)|) ≈ log(log|z(n)|) + log(2)
            // The second logarithm converts exponential growth into approximately linear growth,
            // which allows interpolation between integer iterations.
            //
            // Continuous (smooth) escape-time coloring:
            // Integer iteration counts produce visible color bands because all
            // points escaping on the same iteration receive the same value.
            // To obtain smooth gradients, estimate how far through the final
            // iteration the orbit escaped.
            //
            // The standard normalized iteration count is:
            // ν = n + 1 - log₂(log₂(|z|))
            // where:
            // n = integer escape iteration
            // |z| = orbit magnitude at escape
            //
            // This is derived from the asymptotic growth of the Mandelbrot
            // iteration outside the set:
            // |z(n+1)| ≈ |z(n)|²
            // Taking logarithms:
            // log(|z(n+1)|) ≈ 2·log(|z(n)|)
            // Taking logarithms again:
            // log(log(|z(n+1)|)) ≈ log(log(|z(n)|)) + log(2)
            //
            // The second logarithm converts the exponential growth into an
            // approximately linear quantity, allowing interpolation between
            // integer iterations.
            //
            // Using log₂(|z|) rather than a logarithm involving the bailout
            // radius makes the smoothing largely independent of the chosen
            // bailout and ties the interpolation to the natural growth rate
            // of the Mandelbrot recurrence.
            //
            // Complex magnitude terminology:
            // For a complex number: z = x + yi
            // its magnitude (modulus) is: magnitude = |z| = |x + yi| = sqrt(x² + y²)
            // Various libraries may call this: magnitude = abs(z) = cabs(z)
            // All represent the same quantity: the distance from z to the origin
            // in the complex plane.
            //
            // Logarithm simplification:
            // Since: |z|² = x² + y²
            // and: log(sqrt(x)) = 0.5 * log(x)
            // we can compute: log(|z|) as 0.5 * log(|z|²)
            // avoiding an unnecessary square-root.
            // Therefore: 0.5 * log((zx*zx) + (zy*zy))
            // is mathematically equivalent to: log(|z|)
            //
            // Fractional escape estimate:
            // How far did the orbit travel from the bailout boundary
            // to its actual position after the escape iteration?
            // We want a continuous iteration count rather than a purely integer one.
            // fraction ≈ 0 -> orbit escaped very near the bailout boundary
            // fraction ≈ 1 -> orbit escaped closer to the previous iteration level
            // http://www.fractalforums.com/programming/please-explain-smooth-coloring/
            double fraction = (Math.log(0.5 * Math.log((zx * zx) + (zy * zy))) - CON1) / CON2;

            // Numerical inaccuracies can occasionally make the logarithmic
            // correction slightly larger than its theoretical maximum.
            // Clamping helps avoid histogram glitches when zoomed far out and sideways.
            if (fraction > 1) { // occasionally fraction may exceed 1 slightly
                fraction = 1;
            }
            return count + 1 - fraction;
        } else {
            // No escape detected within maxImageIterations.
            // The point is treated as belonging to the Mandelbrot set (or is
            // indistinguishable from it at the current iteration limit).
            return count;
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

    public void setupKernel(
            float[] iterCountArray,
            LongAdder[] iterCountFreqArray,
            int[] imageArray,
            int[] colorsArray,
            int maxImageIterations,
            int width,
            int height,
            double minA,
            double maxA,
            double minB,
            double maxB,
            int fractalIndex,
            int smooth,
            int histogram,
            int bailout) {

        this.iterCountArray = iterCountArray;
        this.iterCountFreqArray = iterCountFreqArray;
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
    private float[] iterCountArray;
    private LongAdder[] iterCountFreqArray;
    private int smooth;
    private int histogram;
    private int bailout;
    private final double CON1 = 1.23949755335612;       // Math.log(0.5 * Math.log(1000))
    private final double CON2 = 0.6931471805599453;     // Math.log(2)
    private final double CON3 = 1.0986122886681098;     // Math.log(3)
    private final double CON4 = 1.3862943611198906;     // Math.log(4)
    // private final double CON5 = 1.6094379124341003;  // Math.log(5)
}
