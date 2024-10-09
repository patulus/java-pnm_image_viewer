package com.patulus.avendl1q4.gui;

import com.patulus.avendl1q4.Image;

import javax.swing.*;
import java.awt.*;

public class PPMPanel extends JPanel {
    private Image image;

    public PPMPanel(Image image) {
        this.image = image;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(new Color(0, 0, 0));

        int tempR, tempG, tempB;
        for (int i = 0; i < image.height; i++) {
            for (int j = 0; j < image.width; j++) {
                switch (image.getImageType()) {
                    // 바이너리 형식의 PGM 파일
                    case '5':
                        // 최대 음영 값이 8bit이면 해당 픽셀의 음영 값을 그대로 이용
                        if (image.maxClr < 256) {
                            g.setColor(new Color(image.imgGray[i][j], image.imgGray[i][j], image.imgGray[i][j]));
                        // 최대 음영 값이 16bit이면 65535로 나누어 8bit 값으로 변환
                        } else {
                            tempG = image.imgGray[i][j] * 255 / 65535;
                            // 색깔 지정
                            g.setColor(new Color(tempG, tempG, tempG));
                        }
                        break;
                    // 바이너리 형식의 PPM 파일
                    case '6':
                        // 최대 색상 값이 8bit이면 해당 픽셀의 RGB 값을 그대로 이용
                        if (image.maxClr < 256) {
                            g.setColor(new Color(image.imgClrR[i][j], image.imgClrG[i][j], image.imgClrB[i][j]));
                        // 최대 색상 값이 16bit이면 65535로 나누어 8bit 값으로 변환
                        } else {
                            tempR = image.imgClrR[i][j] * 255 / 65535;
                            tempG = image.imgClrG[i][j] * 255 / 65535;
                            tempB = image.imgClrB[i][j] * 255 / 65535;
                            // 색깔 지정
                            g.setColor(new Color(tempR, tempG, tempB));
                        }
                        break;
                }
                // 해당 위치에 지정한 색깔로 점 찍기
                g.fillRect(j, i, 1, 1);
            }
        }

        // 부모 요소를 새로 그리도록 요청
        getParent().revalidate();
        getParent().repaint();
    }
}
