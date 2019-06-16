package com.fourier;

import org.apache.commons.math3.complex.*;
import org.apache.commons.math3.util.ArithmeticUtils;

public class Fourier {

    // Forward fft
    public static Complex[] fftForward(Complex[] input) {
        int n = input.length;
        if (!ArithmeticUtils.isPowerOfTwo((long) n)) {
            throw new IllegalArgumentException("Input of size " + n + " is not a power of two!");
        }
        Complex[] output = new Complex[n];

        if (n == 1) {
            return new Complex[]{input[0]};
        }

        if (n == 2) {
            output[0] = input[0].add(input[1]);
            output[1] = input[0].add(input[1].multiply(-1));
        } else {
            Complex[] inputEven = new Complex[n / 2];
            Complex[] inputOdd = new Complex[n / 2];

            for (int i = 0; i < n / 2; i++) {
                inputEven[i] = input[2 * i];
                inputOdd[i] = input[2 * i + 1];
            }

            Complex[] even = fftForward(inputEven);
            Complex[] odd = fftForward(inputOdd);

            for (int i = 0; i < n / 2; i++) {
                output[i] = even[i].add(getRotation(i, n).multiply(odd[i]));
                output[n / 2 + i] = even[i].add((getRotation(i, n).multiply(odd[i])).multiply(-1));
            }
        }
        return output;
    }

    // Inverse fft
    public static Complex[] fftInverse(Complex[] input) {
        int n = input.length;
        if (!ArithmeticUtils.isPowerOfTwo((long) n)) {
            throw new IllegalArgumentException("Input of size " + n + " is not a power of two!");
        }
        Complex[] output = new Complex[n];

        for (int i = 0; i < n; i++) {
            output[i] = input[i].conjugate();
        }
        output = fftForward(output);
        for (int i = 0; i < n; i++) {
            output[i] = output[i].conjugate();
        }
        for (int i = 0; i < n; i++) {
            output[i] = output[i].divide(n);
        }
        return output;
    }

    // Compute e^(-i*2*PI*k/N)
    private static Complex getRotation(int k, int n) {
        if (k % n == 0) {
            return Complex.ONE;
        }
        double arg = -2 * Math.PI * k / n;
        return new Complex(Math.cos(arg), Math.sin(arg));
    }

}
