# 칼라영상 반전 처리

## 일정
| 일시 | 내용 |
|-----|------|
| '24.09.02, 03. | 레벨1 문제 소개 |
| '24.09.23., 24.| 문제분석및설계서 발표 |
| '24.09.30., 10.02. | 결과 보고서 발표 |

## 개요
칼라영상을 저장하는 파일의 종류는 다양하다. 그 중에서 이진 PPM(raw Portable Pixel Map)은 매우 간단한 헤더를 갖는 구조로 칼라 영상을 저장한다.

3×2 크기를 갖는 파일의 구조는 다음과 같다. 처음 2바이트는 PPM 파일의 식별자(magic number) `P6`의 값을 갖는다. 16진수로는 `0x5036`이다.

다음은 영상의 가로 및 세로 길이이다. 마지막으로 최대 밝기 값이 나타난다. 이 값들은 하나 이상의 공백문자(`blanks`, `TABs`, `CRs`, `LFs`, `VTs`, `FFs`)로 구분된다.

이후에는 픽셀의 값이 나타나는데 칼라 영상에서는 픽셀은 기본 색인 R, G, B의 값으로 구성된다. 픽셀들은 연속적으로 저장된다.

최대 밝기는 1에서 65535 사이의 값을 가진다. 최대 밝기가 256 미만이면 각 픽셀의 기본 색은 1바이트, 256 이상이면 2바이트로 표현된다.

## 기본 문제
PPM 파일을 읽어 영상을 반전한 후에 그 결과를 새로운 PPM 파일에 저장하시오.

## 제한요소 및 요구사항
* 기본 메뉴를 제공한다.
* 상세한 입력과 출력 메시지를 제공한다.
* 2개 이상의 소스파일과 1개 이상의 헤더파일을 사용한다.
* 전역 변수 사용은 가급적 자제한다.
* 그림을 반전 처리한 결과를 파일로 저장한다.

## 확장 문제
사용자로부터 특정 위치를 입력받고 영상을 주어진 위치에 사각형을 출력하고 그 결과를 새로운 PPM 파일에 저장하시오.

회색음영 영상을 저장하는 PGM 파일의 구조를 분석하여 파일 포맷에 따라 PPM 또는 PGM 파일을 처리할 수 있도록 하시오.

## 채점 기준
|범위|구현 기능|배점|비고|
|---|-----|---|----------|
| 기본 기능 | 실행 가능 | 1 | 실행 가능, 초기 화면 출력 |
| | 메뉴 방식 | 1 | 메뉴 방식으로 진행 |
| | 파일 입력 | 1 | 영상 파일 입력 처리 |
| | 영상 파일 처리 | 1 | 파일 구조에 따라 파일 분석 |
| | 반전 처리 | 1 | 반전 처리 |
| | 다중 파일 프로그램 | 1 | 2개 이상의 소스 파일, 1개 이상의 헤더 파일 |
| 확장 기능 | 사각형 출력, 저장 | 1 | 사각형 출력 위치를 사용자 입력으로 처리 |
| | PGM 파일 처리 | 1 |
| 추가 기능 | | 1 | |
| | | 1 | |
