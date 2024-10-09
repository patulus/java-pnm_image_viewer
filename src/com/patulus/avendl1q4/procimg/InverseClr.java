package com.patulus.avendl1q4.procimg;

import com.patulus.avendl1q4.Image;

public class InverseClr {
    public static void execute(Image image) {
        int maxClr = image.maxClr;

        // 색상을 반전하려면 최대 색상 또는 음영 값에서 현재 위치의 값을 빼면 됨
        switch (image.getImageType()) {
            // 바이너리 형식의 PGM 파일
            case '5':
                for (int i = 0; i < image.height; i++) {
                    for (int j = 0; j < image.width; j++) {
                        image.imgGray[i][j] = (maxClr - image.imgGray[i][j]);
                    }
                }
                break;
            // 바이너리 형식의 PPM 파일
            case '6':
                for (int i = 0; i < image.height; i++) {
                    for (int j = 0; j < image.width; j++) {
                        image.imgClrR[i][j] = (maxClr - image.imgClrR[i][j]);
                        image.imgClrG[i][j] = (maxClr - image.imgClrG[i][j]);
                        image.imgClrB[i][j] = (maxClr - image.imgClrB[i][j]);
                    }
                }
                break;
        }
    }
}
