package org.betaiotazeta.fractalmusicgenerator;

public class OrbitCalculator {

    public OrbitCalculator(int maxAudioIterations) {
        this.maxAudioIterations = maxAudioIterations;
        orbitsArray = new ComplexNumber[maxAudioIterations];
    }

    public ComplexNumber[] getOrbitsArray() {
        return orbitsArray;
    }

    public int getMaxAudioIterations() {
        return maxAudioIterations;
    }
    
    public void calculateMandelbrotOrbits(double cx, double cy) {
        int count = 0;
        double zx = cx;
        double zy = cy;
        double zxNext;
        while ((count < maxAudioIterations) && (((zx * zx) + (zy * zy)) < 4)) {
            ComplexNumber zn = new ComplexNumber(zx, zy);            
            orbitsArray[count] = zn;            
            zxNext = ((zx * zx) - (zy * zy)) + cx;
            zy = 2 * zx * zy + cy;
            zx = zxNext;
            count++;
        }
    }
    
    public void calculateBurningShipOrbits(double cx, double cy) {
        int count = 0;
        double zx = cx;
        double zy = cy;
        double zxNext;
        while ((count < maxAudioIterations) && (((zx * zx) + (zy * zy)) < 4)) {
            ComplexNumber zn = new ComplexNumber(zx, zy);
            orbitsArray[count] = zn;
            zxNext = ((zx * zx) - (zy * zy)) + cx;
            zy = Math.abs(2 * zx * zy) + cy;
            zx = zxNext;
            count++;
        }
    }
    
    public void calculateTricornOrbits(double cx, double cy) {
        int count = 0;
        double zx = cx;
        double zy = cy;
        double zxNext;
        while ((count < maxAudioIterations) && (((zx * zx) + (zy * zy)) < 4)) {
            ComplexNumber zn = new ComplexNumber(zx, zy);
            orbitsArray[count] = zn;
            zxNext = zx * zx - zy * zy + cx;
            zy = -2 * zx * zy + cy;
            zx = zxNext;
            count++;
        }
    }
    
    public void calculateRectangleOrbits(double cx, double cy) {
        int count = 0;
        double zx = cx;
        double zy = cy;
        double zxNext;
        while ((count < maxAudioIterations) && (((zx * zx) + (zy * zy)) < 4)) {
            ComplexNumber zn = new ComplexNumber(zx, zy);
            orbitsArray[count] = zn;
            zxNext = zx * zx * zx + zx * zy * zy - zx * cx * cx + zy * cy * cy;
            zy = zx * zx * zy + zy * zy * zy - zx * cy * cy - zy * cx * cx;
            zx = zxNext;
            count++;
        }
    }
 
    public void calculateKlingonOrbits(double cx, double cy) {
        int count = 0;
        double zx = cx;
        double zy = cy;
        double zxNext;
        while ((count < maxAudioIterations) && (((zx * zx) + (zy * zy)) < 4)) {
            ComplexNumber zn = new ComplexNumber(zx, zy);
            orbitsArray[count] = zn;
            zxNext = Math.abs(zx * zx * zx) - 3 * zy * zy * Math.abs(zx) + cx;
            zy = 3 * zx * zx * Math.abs(zy) - Math.abs(zy * zy * zy) + cy;
            zx = zxNext;
            count++;
        }
    }

    public void calculateCrownOrbits(double cx, double cy) {
        int count = 0;
        double zx = cx;
        double zy = cy;
        double zxNext;
        while ((count < maxAudioIterations) && (((zx * zx) + (zy * zy)) < 4)) {
            ComplexNumber zn = new ComplexNumber(zx, zy);
            orbitsArray[count] = zn;
            zxNext = zx * zx * zx - 3 * zx * zy * zy + cx;
            zy = Math.abs(3 * zx * zx * zy - zy * zy * zy) + cy;
            zx = zxNext;
            count++;
        }
    }

    public void calculateFrogOrbits(double cx, double cy) {
        int count = 0;
        double zx = cx;
        double zy = cy;
        double zxNext;
        while ((count < maxAudioIterations) && (((zx * zx) + (zy * zy)) < 4)) {
            ComplexNumber zn = new ComplexNumber(zx, zy);
            orbitsArray[count] = zn;
            zxNext = Math.abs(zx * zx * zx - 3 * zx * zy * zy) + cx;
            zy = Math.abs(3 * zx * zx * zy - zy * zy * zy) + cy;
            zx = zxNext;
            count++;
        }
    }

    public void calculateMandelshipOrbits(double cx, double cy) {
        int count = 0;
        double zx = cx;
        double zy = cy;
        double zxNext;
        while ((count < maxAudioIterations) && (((zx * zx) + (zy * zy)) < 4)) {
            ComplexNumber zn = new ComplexNumber(zx, zy);
            orbitsArray[count] = zn;
            zxNext = -2*cx*zy*zy - 6*zx*zx*zy*zy + zy*zy*zy*zy - 4*cy*zx*zy + cx*cx - cy*cy + zx*zx*zx*zx + 2*cx*zx*zx + cx;
            zy = -2*cy*zy*zy - 4*zx*zy*zy*zy + 4*zx*zx*zx*zy + 4*cx*zx*zy + 2*cx*cy + 2*cy*cx*cx + cy;
            zx = zxNext;
            count++;
        }
    }

    public void calculateFrankensteinOrbits(double cx, double cy) {
        int count = 0;
        double zx = cx;
        double zy = cy;
        double zxNext;
        while ((count < maxAudioIterations) && (((zx * zx) + (zy * zy)) < 4)) {
            ComplexNumber zn = new ComplexNumber(zx, zy);
            orbitsArray[count] = zn;
            zxNext = Math.tanh(zx * zx * zx - 3 * zx * zy * zy) + cx;
            zy = Math.abs(3 * zx * zx * zy - zy * zy * zy) + cy;
            zx = zxNext;
            count++;
        }
    }

    public void calculateLogisticOrbits(double cx, double cy) {
        int count = 0;
        double zx = cx;
        double zy = cy;
        double zxNext;
        while ((count < maxAudioIterations) && (((zx * zx) + (zy * zy)) < 4)) {
            ComplexNumber zn = new ComplexNumber(zx, zy);
            orbitsArray[count] = zn;
            zxNext = -cx*zx*zx + cx*zx + 2*cy*zx*zy + cx*zy*zy - cy*zy;
            zy = cx*zy + cy*zx - cy*zx*zx + cy*zy*zy - 2*cx*zx*zy;
            zx = zxNext;
            count++;
        }
    }    
    
    // Variables
    private int maxAudioIterations;
    ComplexNumber[] orbitsArray;
}
