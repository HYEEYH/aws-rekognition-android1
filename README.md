
![header](https://capsule-render.vercel.app/api?type=waving&color=auto&height=200&section=header&text=Rekognition%20App&fontSize=70)

# aws-rekognition-android1
얼굴 비교 안드로이드 앱
<br/><br/><br/>

* AWS의 Rekognition 기능 중 얼굴 인식과 얼굴 비교 기능을 사용하는 앱 입니다.<br/>
<a href= "https://drive.google.com/file/d/133NNVSuKvDqlj6I9_djBQ8UDfyRwgMLY/view?usp=drive_link">[포트폴리오 보러 가기]</a><br/>
<a href= "https://drive.google.com/file/d/1ANCrtx0upEF--Kr3LH4NQiaXr6qAoksl/view?usp=drive_link">[앱 대시보드 시연영상 보러 가기]</a><br/>
<a href= "https://drive.google.com/file/d/1YCiA3spo6qHxr22r3X_G1uPT__j0eKV6/view?usp=drive_link">[앱 시연영상 보러 가기]</a><br/>
<img src="https://github.com/HYEEYH/aws-rekognition-app2/assets/130967557/d91ae676-0d1f-4d7c-9dca-ba8ba7e65680"  width="700" height="392" /><br/><br/>

## 사용 툴
<div align=center>
<img src="https://img.shields.io/badge/Visual Studio Code-007ACC?style=flat&logo=visualstudiocode&logoColor=white"/>
<img src="https://img.shields.io/badge/Android Studio-3DDC84?style=flat&logo=androidstudio&logoColor=white"/>
<img src="https://img.shields.io/badge/Amazon AWS-232F3E?style=flat-square&logo=amazonaws&logoColor=white"/>
<img src="https://img.shields.io/badge/streamlit-FF4B4B?style=flat-square&logo=streamlit&logoColor=white"> 
</div>

## 사용한 기술
### Back-ends
#### Visual Studio Code (Python)
- 로컬 경로에 디렉토리 생성하여 이미지 파일 저장
- boto3로 S3에 디렉토리 생성하며 이미지 파일 업로드
- S3 버킷에 저장되어 있는 이미지를 가져와서 얼굴 인식 후 결과를 리턴
- 얼굴 인식 결과를 화면에 표시할 때 내가 원하는 결과만 가져옴
- 현재 시간을 서버시간으로 알아내기

##### Streamlit
- 얼굴 인식 페이지와 얼굴 비교 페이지를 나누어 구현
- 유저에게 사진을 입력 받아 얼굴 인식 및 얼굴 비교 코드 실행

### Front-ends
#### Android
- Intent를 사용하여 액티비티간의 정보를 주고 받음
- setOnClickListener를 활용해 사진을 탭 할때 사진 선택이 가능하도록 함
- Config클래스를 만들어 AWS_ACCESS_KEY 사용시 보안 유지
- AWS Rekognition 얼굴비교 함수를 이용해 얼굴 인식 API 이용

### Open API
- AWS Rekognition 얼굴 비교 및 얼굴 인식 기능 구현



##### 
<br/><br/><br/>
