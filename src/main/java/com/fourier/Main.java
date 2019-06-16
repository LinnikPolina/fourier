package com.fourier;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

import java.util.Arrays;

public class Main {

   public static void main(String[] args) {
        int n = 16;


        Complex[] samples = new Complex[n];
        for(int i = 0; i < n; i++){
            samples[i] = new Complex(Math.sin(i) , Math.cos(i));
        }

       int iter = 10000;
       Complex[] out = null;

       double time = System.currentTimeMillis();

       for (int i = 0; i<iter; i++) {
           out = Fourier.fftForward(samples);
       }
       time = System.currentTimeMillis() - time;
       System.out.println(Arrays.toString(out));
       System.out.println("Averaged " + time/iter + "ms per iteration");

       FastFourierTransformer f = new FastFourierTransformer(DftNormalization.STANDARD);
       Complex[] outDefault = null;
       time = System.currentTimeMillis();
       for (int i = 0; i<iter; i++) {
           outDefault = f.transform(samples, TransformType.FORWARD);
       }
       time = System.currentTimeMillis() - time;
       System.out.println(Arrays.toString(outDefault));
       System.out.println("Apache FastFourierTransformer averaged " + time/iter + "ms per iteration");

       double diff =0;
       for (int i = 0; i<n; i++) {
           diff+=outDefault[i].multiply(-1).add(out[i]).abs();
       }

       System.out.println("Abs difference between my and Apache FastFourierTransformer fft: " + diff);
   }
}
