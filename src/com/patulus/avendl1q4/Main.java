package com.patulus.avendl1q4;

import com.patulus.avendl1q4.gui.MainFrame;
import com.patulus.avendl1q4.procimg.InverseClr;
import com.patulus.avendl1q4.procimg.InverseLtRt;
import com.patulus.avendl1q4.procimg.PrtBox;
import com.patulus.avendl1q4.procimg.Rotate;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) throws IOException {
        // withCommand 메서드를 통해 콘솔에서 대화형으로 칼라영상을 처리할 수 있습니다.
//        withCommand();

        // MainFrame 객체를 생성함으로써 GUI를 통해 칼라영상을 처리할 수 있습니다.
        new MainFrame();
    }

    static void withCommand() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("PNM Image Processor v0.0.1 (Created by PARK, YEONJONG / kit CE 20210463)");
        System.out.println("프로그램을 종료하려면 Ctrl + C 또는 Ctrl + D를 누르십시오.");
        System.out.println();

        System.out.print("칼라영상 파일 절대 경로 입력(PBM, PGM, PPM): ");
        String pathname = br.readLine();

        System.out.println("파일을 읽고 있습니다. 잠시만 기다려 주세요...");

        try {
            Image image = new Image(pathname);

            short sel;
            boolean isValid;
            while (true) {
                System.out.println();
                System.out.println("1: 색상 반전, 2: 사각형 출력, 3: 좌우 반전, 4: 90도 회전");
                System.out.println("w: 내보내고 종료, q: 종료");
                System.out.print("메뉴를 선택하세요: ");
                sel = (short) br.readLine().toLowerCase().charAt(0);

                switch (sel) {
                    case '1':
                        System.out.println("색상 반전 처리 중입니다. 잠시만 기다려 주세요...");
                        InverseClr.execute(image);
                        System.out.println("색상 반전 처리를 완료했습니다.");
                        break;
                    case '2':
                        isValid = true;
                        System.out.print("사각형을 출력할 시작 위치와 종료 위치를 입력하세요(띄어쓰기로 구분, 0부터 시작): ");
                        String[] dim = br.readLine().split(" ");
                        if (dim.length < 4) {
                            System.out.println("사각형 출력 위치에 필요한 입력 값을 모두 입력해야 합니다.");
                            break;
                        }
                        int startX = Integer.parseInt(dim[0]);
                        int startY = Integer.parseInt(dim[1]);
                        int endX = Integer.parseInt(dim[2]);
                        int endY = Integer.parseInt(dim[3]);
                        System.out.print("사각형 테두리 굵기를 입력하세요: ");
                        int border = Integer.parseInt(br.readLine());
                        switch (image.getImageType()) {
                            case '5':
                                System.out.print("사각형 테두리 명암을 입력하세요: ");
                                break;
                            case '6':
                                System.out.print("사각형 테두리 색상을 입력하세요: #");
                                break;
                        }
                        String borderHex = br.readLine();

                        if (startX < 0 || startX >= image.width) {
                            System.out.println("잘못된 사각형 시작 열 값입니다.");
                            isValid = false;
                        }
                        if (startY < 0 || startY >= image.height) {
                            System.out.println("잘못된 사각형 시작 행 값입니다.");
                            isValid = false;
                        }
                        if (endX < startX || endX >= image.width) {
                            System.out.println("잘못된 사각형 종료 열 값입니다.");
                            isValid = false;
                        }
                        if (endY < startY || endY >= image.height) {
                            System.out.println("잘못된 사각형 종료 행 값입니다.");
                            isValid = false;
                        }
                        if (border > Math.min(endX - startX, endY - startY)) {
                            System.out.println("잘못된 테두리 굵기입니다.");
                            isValid = false;
                        }
                        switch (image.getImageType()) {
                            case '5':
                                if (Integer.parseInt(borderHex) < 0 || Integer.parseInt(borderHex) > image.maxClr) {
                                    System.out.println("잘못된 테두리 음영 값입니다.");
                                    isValid = false;
                                }
                                break;
                            case '6':
                                if (borderHex.length() < 6) {
                                    System.out.println("잘못된 테두리 헥스 색상 값입니다.");
                                    isValid = false;
                                }
                                break;
                        }

                        if (isValid) {
                            System.out.println("사각형 출력 처리 중입니다. 잠시만 기다려 주세요...");
                            try {
                                PrtBox.execute(image, startX, startY, endX, endY, border, borderHex);
                            } catch (Exception ex) {
                                System.out.println("사각형 출력 처리에 실패했습니다.");
                            }
                            System.out.println("사각형 출력 처리를 완료했습니다.");
                        }
                        break;
                    case '3':
                        System.out.println("좌우 반전 처리 중입니다. 잠시만 기다려 주세요...");
                        try {
                            InverseLtRt.execute(image);
                        } catch (Exception ex) {
                            System.out.println("좌우 반전 처리에 실패했습니다.");
                        }
                        System.out.println("좌우 반전 처리를 완료했습니다.");
                        break;
                    case '4':
                        System.out.print("l: 왼쪽으로 회전, r: 오른쪽으로 회전: ");
                        sel = (short) br.readLine().toLowerCase().charAt(0);
                        try {
                            switch (sel) {
                                case 'l':
                                    System.out.println("90도 회전 처리 중입니다. 잠시만 기다려 주세요...");
                                    Rotate.executeLeft(image);
                                    System.out.println("90도 회전 처리를 완료했습니다.");
                                    break;
                                case 'r':
                                    System.out.println("90도 회전 처리 중입니다. 잠시만 기다려 주세요...");
                                    Rotate.executeRight(image);
                                    System.out.println("90도 회전 처리를 완료했습니다.");
                                    break;
                                default:
                                    System.out.println("메뉴에 없는 항목입니다.");
                            }
                        } catch (Exception ex) {
                            System.out.println("90도 회전 처리에 실패했습니다.");
                        }
                        break;
                    case 'w':
                        System.out.print("내보낼 칼라영상 파일 절대/상대 경로 입력(파일명 및 확장자까지 입력): ");
                        String newPathname = br.readLine();

                        System.out.println("파일을 내보내고 있습니다. 잠시만 기다려 주세요...");
                        try {
                            image.export(newPathname);
                        } catch (Exception ex) {
                            System.out.println("파일 내보내기에 실패했습니다.");
                        }
                        System.out.println("파일 내보내기를 완료했습니다.");
                        System.out.print("내보낸 경로는 다음과 같습니다: " );
                        System.out.println(image.getFile().getAbsolutePath());

                        System.exit(0);
                    case 'q':
                        br.close();
                        System.exit(0);
                    default:
                        System.out.println("메뉴에 없는 항목입니다.");
                }
            }
        } catch (FileNotFoundException ex) {
            System.out.println("파일 읽기에 실패했습니다.");
            System.out.println();
            System.out.println("========================================================================");
            System.out.println();
            withCommand();
        }
    }
}
