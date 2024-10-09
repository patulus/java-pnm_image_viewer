package com.patulus.avendl1q4.gui;

import com.patulus.avendl1q4.Image;

import javax.swing.*;
import java.awt.*;

public class SetBoxDialog extends JPanel {
    public SetBoxDialog(Image image, String[] parameters) {
        setLayout(new BorderLayout(5, 5));

        // 입력 값이 어떤 값인지 사용자에게 표시
        JPanel labels = new JPanel(new GridLayout(6, 1, 2, 2));
        labels.add(new JLabel("Start X (0 ~ " + (image.width - 1) + ")", SwingConstants.TRAILING));
        labels.add(new JLabel("Start Y (0 ~ " + (image.height - 1) + ")", SwingConstants.TRAILING));
        labels.add(new JLabel("End X (Start X ~ " + (image.width - 1) + ")", SwingConstants.TRAILING));
        labels.add(new JLabel("End Y (Start Y ~ " + (image.height - 1) + ")", SwingConstants.TRAILING));
        labels.add(new JLabel("Border Size", SwingConstants.TRAILING));
        switch (image.getImageType()) {
            case '5':
                labels.add(new JLabel("Border Contrast", SwingConstants.TRAILING));
                break;
            case '6':
                labels.add(new JLabel("Border Color Hex", SwingConstants.TRAILING));
                break;
        }

        add(labels, BorderLayout.LINE_START);

        // 입력 값을 입력할 수 있는 박스를 사용자에게 표시
        JPanel controls = new JPanel(new GridLayout(6, 1, 2, 2));
        // 사각형 출력 시작 위치
        JSpinner startXField = new JSpinner(new SpinnerNumberModel(0, 0, image.width - 1, 1));
        controls.add(startXField);

        JSpinner startYField = new JSpinner(new SpinnerNumberModel(0, 0, image.height - 1, 1));
        controls.add(startYField);

        // 사각형 출력 종료 위치
        JSpinner endXField = new JSpinner(new SpinnerNumberModel(image.width - 1, Integer.parseInt(startXField.getValue().toString()), image.width - 1, 1));
        controls.add(endXField);

        JSpinner endYField = new JSpinner(new SpinnerNumberModel(image.height - 1, Integer.parseInt(startYField.getValue().toString()), image.height - 1, 1));
        controls.add(endYField);

        // 사각형 테두리 굵기
        JSpinner borderSizeField = new JSpinner(new SpinnerNumberModel(0, 0, Math.min(Integer.parseInt(endXField.getValue().toString()) - Integer.parseInt(startXField.getValue().toString()), Integer.parseInt(endYField.getValue().toString()) - Integer.parseInt(startYField.getValue().toString())), 1));
        controls.add(borderSizeField);

        // 사각형 음영/색상
        JSpinner borderColorHexFieldForPgm = new JSpinner(new SpinnerNumberModel(0, 0, image.maxClr, 1));
        JTextField borderColorHexFieldForPpm = new JTextField("2196F3");
        switch (image.getImageType()) {
            case '5':
                controls.add(borderColorHexFieldForPgm);
                break;
            case '6':
                controls.add(borderColorHexFieldForPpm);
                break;
        }

        add(controls, BorderLayout.CENTER);

        // OK 버튼이 눌리면 입력된 값을 지정
        if (JOptionPane.showConfirmDialog(getParent(), this, "Print Box Options", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.OK_OPTION) {
            parameters[0] = "";
            parameters[1] = startXField.getValue().toString();
            parameters[2] = startYField.getValue().toString();
            parameters[3] = endXField.getValue().toString();
            parameters[4] = endYField.getValue().toString();
            parameters[5] = borderSizeField.getValue().toString();
            switch (image.getImageType()) {
                case '5':
                    parameters[6] = borderColorHexFieldForPgm.getValue().toString();
                    break;
                case '6':
                    parameters[6] = borderColorHexFieldForPpm.getText();
                    break;
            }
        } else {
            parameters[0] = null;
        }
    }
}
