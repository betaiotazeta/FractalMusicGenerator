package org.betaiotazeta.fractalmusicgenerator;

public class Example {
    
    /*
        public void calculateFractalNonAparapi() {
        
        // NOT USED, IT SERVES AS A REMAINDER ON HOW THINGS WORK
        // this method can be copied in FractalPanel class

    
//        Mandelbrot definition:
//        f(z) = z*z + c
//        initial value of z = 0
//        then we iterate replacing the returned value as z
//        as long as the value does not diverge (does not tend towards infinity)
//    
//        z1 = 0 + c = c
//        z2 = c*c + c   [= z1*z1 + c]
//        z3 = (c*c + c) * (c*c + c) + c   [= z2*z2 + c]
//        ...return value is the new value for z
//        z is an iteration point on the coordinate plain
//    
//        c is a complex number, c = (a + bi)
//        c*c = (a + bi) * (a + bi) = a*a + abi + abi + b*b*i*i
//        knowing that: [i*i = -1]
//        = a*a + 2abi - b*b = a*a - b*b + 2abi
//        real component = a*a - b*b
//        imaginary component = 2ab [i]

            
        // scale(final double valueIn, final double baseMin, final double baseMax, final double limitMin, final double limitMax)
        // valueOut = ((limitMax - limitMin) * (valueIn - baseMin) / (baseMax - baseMin)) + limitMin
        // double zx = Utilities.scale(px, 0, panelWidth, minA, maxA);
        // double zy = Utilities.scale(py, 0, panelHeight, maxB, minB);
        // ----------------------------------------------------- //
        
        // set all these to correct values for the image
        final int width = this.getWidth();
        final int height = this.getHeight();

        imageFractal = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);
        final int[] imageArray = ((DataBufferInt) imageFractal.getRaster().getDataBuffer()).getData();

        final int baseMinX = 0;
        final int baseMaxX = width;
        final double limitMinX = minA;
        final double limitMaxX = maxA;

        final int baseMinY = 0;
        final int baseMaxY = height;
        final double limitMinY = maxB;
        final double limitMaxY = minB;

        // ----------------------------------------------------- //
        // gid is the variable! like i in a loop
        // final int gid = 0; // getGlobalId();
        for (int gid = 0; gid < width * height; gid++) {

            // width * height = max gid
            // in an image values are stored line after line from top to bottom
            final int px = (gid % width);
            final int py = (gid / width);

            float zx = (float) (((limitMaxX - limitMinX) * (px - baseMinX) / (baseMaxX - baseMinX)) + limitMinX);
            float zy = (float) (((limitMaxY - limitMinY) * (py - baseMinY) / (baseMaxY - baseMinY)) + limitMinY);

            // ----------------------------------------------------- //
            int count = 0;

            float cx = zx;
            float cy = zy;
            float zxNext = 0f;

            // iterate until Z orbit diverges too far or until maxImageIterations is reached
            while ((count < maxImageIterations) && (((zx * zx) + (zy * zy)) < 4)) {
                zxNext = ((zx * zx) - (zy * zy)) + cx;
                zy = (2 * zx * zy) + cy;
                zx = zxNext;
                count++;
            }

            // Put the value from the precalculated palette for this iteration count
            imageArray[gid] = colorsArray[count];
        }
    }
    */
}
