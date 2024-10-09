package com.patulus.avendl1q4.gui;

import com.patulus.avendl1q4.Image;
import com.patulus.avendl1q4.procimg.InverseClr;
import com.patulus.avendl1q4.procimg.InverseLtRt;
import com.patulus.avendl1q4.procimg.PrtBox;
import com.patulus.avendl1q4.procimg.Rotate;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

public class MainFrame extends JFrame {
    private final Container container = getContentPane();

    private final Toolkit kit = Toolkit.getDefaultToolkit();
    private final Dimension screenSize = kit.getScreenSize();

    private final JMenuBar menuBar = new JMenuBar();
    private JMenuItem[][] menuItems;
    private final JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));

    private File file;
    private Image image;

    private final String applicationTitle = "PNM Image Processor v0.0.1 (Made by PARK, Yeonjong / 20210463 / kit CE)";
    private final int defaultWidth = 500;
    private final int defaultHeight = 500;

    public MainFrame() {
        init();
        createMenu();

        setVisible(true);
    }

    private void init() {
        container.setLayout(null);

        // GUI 타이틀 설정
        setTitle(applicationTitle);
        // GUI 최소 크기 및 현재 크기 설정
        setMinimumSize(new Dimension(defaultWidth, defaultHeight));
        setSize(defaultWidth, defaultHeight);
        // 화면 중앙에 나타나도록 설정
        setLocationRelativeTo(null);
        // 닫기 버튼 시 동작 설정
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void createMenu() {
        // File 메뉴 생성 및 2차 메뉴 추가
        JMenu fileMenu = new JMenu("File");
        String[] fileMenuItemNames = {
                "Open...",
                "Close Image",
                "^SEPARATOR",
                "Export...",
                "^SEPARATOR",
                "Exit"
        };
        JMenuItem[] fileMenuItems = new JMenuItem[fileMenuItemNames.length];
        FileMenuActionListener fileMenuActionListener = new FileMenuActionListener();
        for (int i = 0; i < fileMenuItems.length; i++) {
            // 구분선 추가
            if (fileMenuItemNames[i].equals("^SEPARATOR")) {
                fileMenu.add(new JSeparator());
            } else {
            // 메뉴 추가
                fileMenuItems[i] = new JMenuItem(fileMenuItemNames[i]);
                fileMenuItems[i].addActionListener(fileMenuActionListener);
                fileMenu.add(fileMenuItems[i]);
            }
        }
        // 파일을 불러왔을 때 활성화되도록 기본적으로 비활성화
        menuEnabled(fileMenuItems, false, 1, 3);

        // Edit 메뉴 생성 및 2차 메뉴 추가
        JMenu editMenu = new JMenu("Edit");
        String[] editMenuItemNames = {
                "Inverse Colors",
                "Print Box...",
                "Inverse Left/Right",
                "Rotate 90 Degrees (Left)",
                "Rotate 90 Degrees (Right)"
        };
        JMenuItem[] editMenuItems = new JMenuItem[editMenuItemNames.length];
        EditMenuActionListener editMenuActionListener = new EditMenuActionListener();
        for (int i = 0; i < editMenuItems.length; i++) {
            editMenuItems[i] = new JMenuItem(editMenuItemNames[i]);
            editMenuItems[i].addActionListener(editMenuActionListener);
            editMenuItems[i].setEnabled(false);
            editMenu.add(editMenuItems[i]);
        }
        // 파일을 불러왔을 때 활성화되도록 기본적으로 비활성화
        menuEnabled(editMenuItems, false, 0, 1, 2, 3, 4);

        // 내부 클래스에서 쉽게 사용할 수 있게 하기 위함
        menuItems = new JMenuItem[2][];
        menuItems[0] = fileMenuItems;
        menuItems[1] = editMenuItems;

        // 1차 메뉴 추가
        menuBar.add(fileMenu);
        menuBar.add(editMenu);

        // 메뉴바 설정
        setJMenuBar(menuBar);
    }

    // 메뉴 활성화 함수
    private void menuEnabled(Component[] menuItems, boolean b, int... idx) {
        // idx들에 위치한 JMenuItem을 활성화
        for (int i : idx) {
            menuItems[i].setEnabled(b);
        }
    }

    private class FileMenuActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // 이벤트가 발생한 메뉴 이름 확인
            String clickedMenu = e.getActionCommand();
            int fileChooserReturnValue;

            switch (clickedMenu) {
                case "Open...":
                    // 선택 가능한 파일 확장자 초기화
                    fileChooser.resetChoosableFileFilters();
                    // 표시할 파일 확장자 외 파일 확장자를 선택할 수 없도록 설정
                    fileChooser.setAcceptAllFileFilterUsed(false);
                    // 다중 선택이 불가하도록 설정
                    fileChooser.setMultiSelectionEnabled(false);

                    // *.pgm 파일만 표시
                    fileChooser.setFileFilter(new FileNameExtensionFilter("PGM (*.pgm)", "pgm"));
                    // *.ppm 파일만 표시
                    fileChooser.setFileFilter(new FileNameExtensionFilter("PPM (*.ppm)", "ppm"));
                    // *.pnm, *.pgm, *.ppm 파일만 표시
                    fileChooser.setFileFilter(new FileNameExtensionFilter("모든 PNM 파일", "pnm", "pgm", "ppm"));

                    // 파일 탐색창 표시
                    fileChooserReturnValue = fileChooser.showOpenDialog(null);
                    // 파일 열기 버튼을 누르지 않았다면 이벤트 처리하지 않기
                    if (fileChooserReturnValue != JFileChooser.APPROVE_OPTION) return;

                    // 선택한 파일 경로 얻기
                    file = fileChooser.getSelectedFile();

                    // 화면 정리
                    getContentPane().removeAll();
                    revalidate();
                    repaint();

                    // 파일을 불러와 화면에 표시
                    // 불러올 파일이 없으면 이벤트 처리하지 않기
                    if (file == null) return;

                    try {
                        // 사용자에게 파일을 불러오고 있다고 표시
                        setTitle("Loading... | " + applicationTitle);
                        // 칼라영상 불러오기
                        image = new Image(file.getAbsolutePath());
                        // 모두 불러왔으면 파일 이름을 표시
                        setTitle(file.getName() + " | " + applicationTitle);

                        // 파일을 불러왔으므로 파일 닫기 및 칼라영상 처리가 가능하도록 설정
                        menuEnabled(menuItems[0], true, 1, 3);
                        menuEnabled(menuItems[1], true, 0, 1, 2, 3, 4);

                        // 칼라영상 크기가 모니터 해상도보다 크면 안내창 띄우기
                        if (image.width >= screenSize.width * 0.95 || image.height >= screenSize.height * 0.95) {
                            JOptionPane.showMessageDialog(getParent(), "불러온 칼라영상이 현재 모니터의 해상도와 맞지 않아 표시할 수 없습니다.\n화면에 표시되지 않더라도 칼라영상을 처리할 수 있습니다.", "칼라영상을 표시할 수 없음", JOptionPane.WARNING_MESSAGE);
                            return;
                        }

                        // 칼라영상을 화면에 표시
                        PPMPanel ppmPanel = new PPMPanel(image);
                        // GUI 크기를 칼라영상 크기에 맞추기
                        setFrameSize();
                        ppmPanel.setSize(image.width, image.height);
                        container.add(ppmPanel);
                        ppmPanel.setBounds(5, 5, image.width, image.height);
                        // GUI를 화면 중앙에 정렬
                        setLocationRelativeTo(null);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(getParent(), ex.getMessage(), "칼라영상을 표시할 수 없음", JOptionPane.ERROR_MESSAGE);
                        throw new RuntimeException(ex);
                    }
                    break;
                case "Close Image":
                    // 파일과 이미지 객체를 가리키지 않도록 하여 가비지 컬렉션이 정리하도록 함
                    file = null;
                    image = null;

                    // 특정 메뉴 비활성화
                    menuEnabled(menuItems[0], false, 1, 3);
                    menuEnabled(menuItems[1], false, 0, 1, 2, 3, 4);

                    setTitle(applicationTitle);

                    // 화면 정리
                    getContentPane().removeAll();
                    revalidate();
                    repaint();

                    // GUI 크기를 기본 크기로 설정
                    setFrameSize();

                    // GUI를 화면 중앙에 정렬
                    setLocationRelativeTo(null);
                    break;
                case "Export...":
                    // 선택 가능한 파일 확장자 초기화
                    fileChooser.resetChoosableFileFilters();

                    // 불러온 칼라영상에 따라 내보내기 가능한 확장자 설정
                    if (image.getImageType() == '5') {
                        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("PGM (*.pgm)", "pgm"));
                    } else if (image.getImageType() == '6') {
                        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("PPM (*.ppm)", "ppm"));
                    }
                    fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("PNM (*.pnm)", "pnm"));

                    // 파일 저장창 표시
                    fileChooserReturnValue = fileChooser.showSaveDialog(null);
                    // 파일 저장 버튼을 누르지 않았다면 이벤트 처리하지 않기
                    if (fileChooserReturnValue != JFileChooser.APPROVE_OPTION) return;
                    // 선택된 파일 경로 얻기
                    File exportFile = fileChooser.getSelectedFile();

                    try {
                        // 파일 내보내기
                        image.export(fileChooser.getSelectedFile().getAbsolutePath());
                        setTitle(exportFile.getName() + " | " + applicationTitle);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(getParent(), ex.getMessage(), "칼라영상을 표시할 수 없음", JOptionPane.ERROR_MESSAGE);
                        throw new RuntimeException(ex);
                    }
                    break;
                case "Exit":
                    // 프로그램 종료
                    System.exit(0);
            }
        }
    }

    private void setFrameSize() {
        // 불러온 칼라영상이 없으면 기본 크기로 설정
        if (image == null) {
            setSize(defaultWidth, defaultHeight);
            return;
        }

        // 칼라영상 가로 크기가 기본보다 작고, 세로 크기가 기본보다 크면 세로 크기만 조절
        if (image.width + 25 < defaultWidth && image.height + 75 > defaultHeight) {
            setSize(defaultWidth, image.height + 75);
        // 칼라영상 세로 크기가 기본보다 작고, 가로 크기가 기본보다 크면 가로 크기만 조절
        } else if (image.width + 25 > defaultWidth  && image.height + 75 < defaultHeight) {
            setSize(image.width + 25, defaultHeight);
        // 칼라영상 가로 세로 크기가 기본보다 작으면 기본 크기로 설정
        } else if (image.width + 25 < defaultWidth && image.height + 75 < defaultHeight) {
            setSize(defaultWidth, defaultHeight);
        // 칼라 영상 가로 세로 크기를 칼라영상에 맞게 설정
        } else {
            setSize(image.width + 25, image.height + 75);
        }
    }

    private class EditMenuActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // 이벤트가 발생한 메뉴 이름 확인
            String clickedMenu = e.getActionCommand();

            // 불러온 파일이 없으면 예외 발생
            if (file == null) {
                JOptionPane.showMessageDialog(getParent(), "칼라영상을 불러오지 않아 처리할 수 없습니다.", "칼라영상을 처리할 수 없음", JOptionPane.ERROR_MESSAGE);
                throw new RuntimeException("칼라영상을 불러오지 않아 처리할 수 없습니다.");
            }

            switch (clickedMenu) {
                case "Inverse Colors":
                    // 칼라영상 반전
                    InverseClr.execute(image);
                    break;
                case "Print Box...":
                    // 사각형 출력 위치, 사각형 외곽선 크기 및 색상 설정
                    String[] parameters = new String[7];
                    // 설정창 띄우기
                    new SetBoxDialog(image, parameters);

                    // 아무것도 설정되지 않았으면 무시, 설정되었으면 사각형 출력
                    if (parameters[0] != null) {
                        int startX = Integer.parseInt(parameters[1]);
                        int startY = Integer.parseInt(parameters[2]);
                        int endX = Integer.parseInt(parameters[3]);
                        int endY = Integer.parseInt(parameters[4]);
                        int border = Integer.parseInt(parameters[5]);
                        String borderHex = parameters[6];
                        if (image.getImageType() == '6' && borderHex.length() < 6) {
                            JOptionPane.showMessageDialog(getParent(), "6자리의 모든 헥스 색상 값을 입력해야 합니다.", "칼라영상을 처리할 수 없음", JOptionPane.WARNING_MESSAGE);
                            return;
                        }

                        PrtBox.execute(image, startX, startY, endX, endY, border, borderHex);
                    }
                    break;
                case "Inverse Left/Right":
                    // 좌우 반전
                    InverseLtRt.execute(image);
                    break;
                case "Rotate 90 Degrees (Left)":
                    // 90도 왼쪽으로 회전
                    Rotate.executeLeft(image);
                    break;
                case "Rotate 90 Degrees (Right)":
                    // 90도 오른쪽으로 회전
                    Rotate.executeRight(image);
                    break;
            }

            // 화면 정리
            getContentPane().removeAll();
            revalidate();
            repaint();

            // 처리 후 칼라영상 크기가 모니터 해상도보다 크면 안내창 띄우기
            if (image.width >= screenSize.width * 0.95 || image.height >= screenSize.height * 0.95) {
                JOptionPane.showMessageDialog(getParent(), "불러온 칼라영상이 현재 모니터의 해상도와 맞지 않아 표시할 수 없습니다.\n화면에 표시되지 않더라도 칼라영상을 처리할 수 있습니다.", "칼라영상을 표시할 수 없음", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 처리된 칼라영상 화면에 표시
            PPMPanel ppmPanel = new PPMPanel(image);
            // GUI 크기를 칼라영상 크기에 맞추기
            setSize(image.width + 25, image.height + 75);
            ppmPanel.setSize(image.width, image.height);
            add(ppmPanel);
            ppmPanel.setBounds(5, 5, image.width, image.height);
            ppmPanel.setVisible(true);
            // GUI를 화면 중앙에 정렬
            setLocationRelativeTo(null);
            setVisible(true);
        }
    }
}
