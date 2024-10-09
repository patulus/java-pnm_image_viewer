package com.patulus.avendl1q4.procimg;

import com.patulus.avendl1q4.Image;

public class InverseLtRt {
    public static void execute(Image image) {
        // [그림] https://url.kr/dgmujb
        switch (image.getImageType()) {
            // 바이너리 형식의 PGM 파일
            case '5':
                int tempGray;
                for (int i = 0; i < image.height; i++) {
                    for (int j = 0; j < image.width / 2; j++) {
                        tempGray = image.imgGray[i][image.width - 1 - j];

                        image.imgGray[i][image.width - 1 - j] = image.imgGray[i][j];
                        image.imgGray[i][j] = tempGray;
                    }
                }
                break;
            // 바이너리 형식의 PPM 파일
            case '6':
                int tempClrR, tempClrG, tempClrB;
                for (int i = 0; i < image.height; i++) {
                    for (int j = 0; j < image.width / 2; j++) {
                        tempClrR = image.imgClrR[i][image.width - 1 - j];
                        tempClrG = image.imgClrG[i][image.width - 1 - j];
                        tempClrB = image.imgClrB[i][image.width - 1 - j];

                        image.imgClrR[i][image.width - 1 - j] = image.imgClrR[i][j];
                        image.imgClrG[i][image.width - 1 - j] = image.imgClrG[i][j];
                        image.imgClrB[i][image.width - 1 - j] = image.imgClrB[i][j];
                        image.imgClrR[i][j] = tempClrR;
                        image.imgClrG[i][j] = tempClrG;
                        image.imgClrB[i][j] = tempClrB;
                    }
                }
                break;
        }
    }
}
