package com.patulus.avendl1q4.procimg;

import com.patulus.avendl1q4.Image;

public class Rotate {
    public static void executeLeft(Image image) {
        int originWidth = image.width;
        int originHeight = image.height;
        int i, j;

        // [그림] https://url.kr/dgmujb
        switch (image.getImageType()) {
            // 바이너리 형식의 PGM 파일
            case '5':
                int[][] imgGray = new int[originWidth][originHeight];

                for (i = 0; i < originHeight; i++) {
                    for (j = 0; j < originWidth; j++) {
                        imgGray[originWidth - 1 - j][i] = image.imgGray[i][j];
                    }
                }
                image.imgGray = imgGray;
                break;
            // 바이너리 형식의 PPM 파일
            case '6':
                int[][] imgClrR = new int[originWidth][originHeight];
                int[][] imgClrG = new int[originWidth][originHeight];
                int[][] imgClrB = new int[originWidth][originHeight];

                for (i = 0; i < originHeight; i++) {
                    for (j = 0; j < originWidth; j++) {
                        imgClrR[originWidth - 1 - j][i] = image.imgClrR[i][j];
                        imgClrG[originWidth - 1 - j][i] = image.imgClrG[i][j];
                        imgClrB[originWidth - 1 - j][i] = image.imgClrB[i][j];
                    }
                }
                image.imgClrR = imgClrR;
                image.imgClrG = imgClrG;
                image.imgClrB = imgClrB;
                break;
        }

        image.width = originHeight;
        image.height = originWidth;
    }

    public static void executeRight(Image image) {
        int originWidth = image.width;
        int originHeight = image.height;
        int i, j;

        switch (image.getImageType()) {
            // 바이너리 형식의 PGM 파일
            case '5':
                int[][] imgGray = new int[originWidth][originHeight];

                for (i = 0; i < originHeight; i++) {
                    for (j = 0; j < originWidth; j++) {
                        imgGray[j][originHeight - 1 - i] = image.imgGray[i][j];
                    }
                }
                image.imgGray = imgGray;
                break;
            // 바이너리 형식의 PPM 파일
            case '6':
                int[][] imgClrR = new int[originWidth][originHeight];
                int[][] imgClrG = new int[originWidth][originHeight];
                int[][] imgClrB = new int[originWidth][originHeight];

                for (i = 0; i < originHeight; i++) {
                    for (j = 0; j < originWidth; j++) {
                        imgClrR[j][originHeight - 1 - i] = image.imgClrR[i][j];
                        imgClrG[j][originHeight - 1 - i] = image.imgClrG[i][j];
                        imgClrB[j][originHeight - 1 - i] = image.imgClrB[i][j];
                    }
                }
                image.imgClrR = imgClrR;
                image.imgClrG = imgClrG;
                image.imgClrB = imgClrB;
                break;
        }

        image.width = originHeight;
        image.height = originWidth;
    }
}
