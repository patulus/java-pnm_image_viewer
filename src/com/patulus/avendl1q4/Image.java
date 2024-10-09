package com.patulus.avendl1q4;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Image {
    private File file; // 불러온 파일
    private char[] fileType; // 영상 식별자

    public int width; // 가로 길이
    public int height; // 세로 길이
    public int maxClr; // 최대 색상/음영 값

    public int[][] imgGray; // 회색음영 영상 정보
    public int[][] imgClrR; // R 채널 영상 정보
    public int[][] imgClrG; // G 채널 영상 정보
    public int[][] imgClrB; // B 채널 영상 정보

    public Image(String pathname) throws IOException {
        this.file = new File(pathname);
        this.read();
    }

    private void read() throws IOException {
        FileInputStream isr = new FileInputStream(this.file);

        int buffer = 0, bufferB = 0;
        int i, j;

        // PNM 파일 식별자 확인
        this.fileType = new char[2];
        for (i = 0; i < 2; i++) {
            this.fileType[i] = (char) isr.read();
        }
        // 식별자가 P로 시작하지 않으면 PNM 파일이 아님
        if (this.fileType[0] != 'P') throw new IOException("Not a PNM(PBM/PGM/PPM) file");
        // [ASCII 방식] PBM P1, PGM P2, PPM P3 / [Binary 방식] PBM P4, PGM P5, PPM P6
        // 바이너리 방식의 PGM, PPM 파일만 지원
        if (this.fileType[1] < '5' || this.fileType[1] > '6') throw new IOException("Not a supported PGM/PPM file");

        // 불필요한 공백 제거 및 이미지의 가로 길이 읽기
        buffer = skipWhitespace(isr, buffer);
        this.width = readInt(isr, buffer);
        // 불필요한 공백 제거 및 이미지의 세로 길이 읽기
        buffer = skipWhitespace(isr, buffer);
        this.height = readInt(isr, buffer);

        // 불필요한 공백 제거 및 이미지의 최대 색상/명암 값 확인
        buffer = skipWhitespace(isr, buffer);
        this.maxClr = readInt(isr, buffer);

        // 불필요한 공백 제거
        buffer = skipWhitespace(isr, buffer);

        // 식별자에 따라 읽기 방식을 다르게 함
        switch (this.fileType[1]) {
            // 바이너리 방식의 PGM 파일
            case '5':
                this.imgGray = new int[this.height][this.width];

                for (i = 0; i < this.height; i++) {
                    for (j = 0; j < this.width; j++) {
                        // 최대 음영 값이 8bit이면 한 바이트를 읽고
                        if (maxClr < 256) {
                            this.imgGray[i][j] = buffer;
                            buffer = isr.read();
                        // 최대 음영 값이 8bit 초과 16bit 이하이면 두 바이트를 읽음
                        // 값을 읽을 때 값을 합쳐서 읽음
                        } else {
                            bufferB = isr.read();
                            this.imgGray[i][j] = (buffer << 8) | bufferB;
                            buffer = isr.read();
                        }
                        // 예상치 못한 EOF를 만나면 중지
                        if (buffer == -1) break;
                    }
                }
                break;
            // 바이너리 방식의 PPM 파일
            case '6':
                this.imgClrR = new int[this.height][this.width];
                this.imgClrG = new int[this.height][this.width];
                this.imgClrB = new int[this.height][this.width];

                for (i = 0; i < this.height; i++) {
                    for (j = 0; j < this.width; j++) {
                        // 최대 색상 값이 8bit이면 한 바이트씩 R, G, B 세 번 읽음
                        if (maxClr < 256) {
                            this.imgClrR[i][j] = buffer;
                            buffer = isr.read();
                            this.imgClrG[i][j] = buffer;
                            buffer = isr.read();
                            this.imgClrB[i][j] = buffer;
                            buffer = isr.read();
                        // 최대 색상 값이 16bit이면 두 바이트씩 R, G, B 세 번 읽음
                        // 값을 읽을 때 값을 합쳐서 읽음
                        } else {
                            bufferB = isr.read();
                            this.imgClrR[i][j] = (buffer << 8) | bufferB;
                            buffer = isr.read();
                            bufferB = isr.read();
                            this.imgClrG[i][j] = (buffer << 8) | bufferB;
                            buffer = isr.read();
                            bufferB = isr.read();
                            this.imgClrB[i][j] = (buffer << 8) | bufferB;
                            buffer = isr.read();
                        }
                    }
                }
                break;
        }
    }

    private boolean isWhitespace(int b) {
        return b == 0x20        // SPC 공백 문자
                || b == 0x09    // HT  수평 탭
                || b == 0x0A    // LF  개행
                || b == 0x0B    // VT  수직 탭
                || b == 0x0C    // FF  다음 페이지
                || b == 0x0D;   // CR  복귀
    }

    private int skipWhitespace(FileInputStream isr, int buffer) throws IOException {
        // 공백이면 입력 스트림에서 계속 읽고,
        // 공백이 아니면 멈추고 해당 값을 반환
        do {
            buffer = isr.read();
        } while (isWhitespace(buffer));

        return buffer;
    }

    private int readInt(FileInputStream isr, int buffer) throws IOException {
        int res = 0;

        while (!isWhitespace(buffer)) {
            // 불필요한 주석을 입력 스트림에서 제거
            if (buffer == '#') {
                while (buffer != '\n') {
                    buffer = isr.read();
                }
                buffer = isr.read();
                continue;
            }

            // 예: 256이면 2를 읽을 때 res가 0+2, 5를 읽을 때 20+5, 6을 읽을 때 250+6
            res = res * 10 + buffer - '0';

            buffer = isr.read();
        }

        return res;
    }

    public void export(String pathname) throws IOException {
        File newFile = new File(pathname);
        FileOutputStream osr = new FileOutputStream(newFile);

        // 헤더 쓰기
        StringBuilder header = new StringBuilder();
        // 읽어 온 파일의 식별자를 그대로 내보냄
        header.append(fileType[0]).append(fileType[1]).append('\n');
        // 처리 후 영상의 가로 길이와 세로 길이를 내보냄
        header.append(width).append(' ').append(height).append('\n');
        // 읽어 온 영상의 최대 색상/음영 값을 그대로 내보냄
        header.append(maxClr).append('\n');
        // 저장할 파일에 덮어 씀
        osr.write(header.toString().getBytes());

        // 영상 정보의 가로 길이씩 파일에 추가로 덮어 씀
        byte[] rows = null;
        switch (this.fileType[1]) {
            // 바이너리 방식의 PGM 파일
            case '5':
                // 최대 음영 값이 8bit 이하면 가로 길이만큼만 할당
                if (maxClr < 256) {
                    rows = new byte[this.width];
                // 최대 음영 값이 8bit 초과 16bit 이하면 가로 길이에 각 픽셀 당 2바이트 할당
                } else {
                    rows = new byte[this.width * 2];
                }
                break;
            // 바이너리 방식의 PPM 파일
            case '6':
                // 최대 색상 값이 8bit 이하면 가로 길이에 (각 픽셀 당 R, G, B 채널 1바이트해서 3바이트) 할당
                if (maxClr < 256) {
                    rows = new byte[this.width * 3];
                // 최대 색상 값이 8bit 초과 16bit 이하면 가로 길이에 (각 픽셀 당 R, G, B 채널 2바이트해서 6바이트) 할당
                } else {
                    rows = new byte[this.width * 3 * 2];
                }
                break;
        }

        // 영상 정보 쓰기
        int i, j, k;
        switch (this.fileType[1]) {
            // 바이너리 방식의 PGM 파일
            case '5':
                for (i = 0; i < this.height; i++) {
                    k = 0;
                    for (j = 0; j < this.width; j++) {
                        // 최대 음영 값이 8bit 이하면 해당 값을 내보냄
                        if (maxClr < 256) {
                            rows[k++] = (byte) imgGray[i][j];
                        // 최대 음영 값이 8bit 초과 16bit 이하면 값을 분할해 내보냄
                        } else {
                            rows[k++] = (byte) ((imgGray[i][j] >> 8) & 255);
                            rows[k++] = (byte) (imgGray[i][j] & 255);
                        }
                    }
                    osr.write(rows);
                }
                break;
            // 바이너리 방식의 PPM 파일
            case '6':
                for (i = 0; i < this.height; i++) {
                    k = 0;
                    for (j = 0; j < this.width; j++) {
                        // 최대 색상 값이 8bit 이하면 해당 값을 내보냄
                        if (maxClr < 256) {
                            rows[k++] = (byte) imgClrR[i][j];
                            rows[k++] = (byte) imgClrG[i][j];
                            rows[k++] = (byte) imgClrB[i][j];
                        // 최대 색상 값이 8bit 초과 16bit 이하면 값을 분할해 내보냄
                        } else {
                            rows[k++] = (byte) ((imgClrR[i][j] >> 8) & 255);
                            rows[k++] = (byte) (imgClrR[i][j] & 255);
                            rows[k++] = (byte) ((imgClrG[i][j] >> 8) & 255);
                            rows[k++] = (byte) (imgClrG[i][j] & 255);
                            rows[k++] = (byte) ((imgClrB[i][j] >> 8) & 255);
                            rows[k++] = (byte) (imgClrB[i][j] & 255);
                        }
                    }
                    osr.write(rows);
                }
                break;
        }

        this.file = newFile;

        osr.close();
    }

    public File getFile() {
        return file;
    }

    public char getImageType() {
        return fileType[1];
    }
}
