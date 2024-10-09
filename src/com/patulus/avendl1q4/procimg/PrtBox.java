package com.patulus.avendl1q4.procimg;

import com.patulus.avendl1q4.Image;

public class PrtBox {
    public static void execute(Image image, int startX, int startY, int endX, int endY, int border, String borderHex) {
        int i, j, tempR, tempG, tempB;

        // [그림] https://url.kr/dgmujb
        switch (image.getImageType()) {
            // 바이너리 형식의 PGM 파일
            case '5':
                tempG = Integer.parseInt(borderHex);

                for (i = startY; i < startY + border; i++) {
                    for (j = startX; j <= endX; j++) {
                        image.imgGray[i][j] = tempG;
                    }
                }
                for (i = endY - border + 1; i <= endY; i++) {
                    for (j = startX; j <= endX; j++) {
                        image.imgGray[i][j] = tempG;
                    }
                }
                for (i = startY; i <= endY; i++) {
                    for (j = startX; j < startX + border; j++) {
                        image.imgGray[i][j] = tempG;
                    }
                }
                for (i = startY; i <= endY; i++) {
                    for (j = endX - border + 1; j <= endX; j++) {
                        image.imgGray[i][j] = tempG;
                    }
                }
                break;
            // 바이너리 형식의 PPM 파일
            case '6':
                if (image.maxClr < 256) {
                    tempR = Integer.parseInt(borderHex.substring(0, 2), 16);
                    tempG = Integer.parseInt(borderHex.substring(2, 4), 16);
                    tempB = Integer.parseInt(borderHex.substring(4, 6), 16);
                } else {
                    tempR = Integer.parseInt(borderHex.substring(0, 2), 16) * 257;
                    tempG = Integer.parseInt(borderHex.substring(2, 4), 16) * 257;
                    tempB = Integer.parseInt(borderHex.substring(4, 6), 16) * 257;
                }
                for (i = startY; i < startY + border; i++) {
                    for (j = startX; j <= endX; j++) {
                        image.imgClrR[i][j] = tempR;
                        image.imgClrG[i][j] = tempG;
                        image.imgClrB[i][j] = tempB;
                    }
                }
                for (i = endY - border + 1; i <= endY; i++) {
                    for (j = startX; j <= endX; j++) {
                        image.imgClrR[i][j] = tempR;
                        image.imgClrG[i][j] = tempG;
                        image.imgClrB[i][j] = tempB;
                    }
                }
                for (i = startY; i <= endY; i++) {
                    for (j = startX; j < startX + border; j++) {
                        image.imgClrR[i][j] = tempR;
                        image.imgClrG[i][j] = tempG;
                        image.imgClrB[i][j] = tempB;
                    }
                }
                for (i = startY; i <= endY; i++) {
                    for (j = endX - border + 1; j <= endX; j++) {
                        image.imgClrR[i][j] = tempR;
                        image.imgClrG[i][j] = tempG;
                        image.imgClrB[i][j] = tempB;
                    }
                }
                break;
        }
    }
}
