# Foreground 서비스

### 내용
- 포어그라운드 서비스 등록
  - Notification 공지
- 디바이스를 껐다 켜도 포어그라운드 서비스가 시작되도록 설정

### 참고사항
- FLAG_IMMUTABLE // api level 31부터 필요
- Manifest.permission.FOREGROUND_SERVICE, // api level 28 이상이어야 함
- 권한 요청 팝업 수락 시, 권한 부여가 즉시 이루어지지 않으며, 앱이 다시 시작될 때까지 기다려야 한다
  - 출처 : https://developer.android.com/training/permissions/requesting
  - 이에 따라 권한 수락 이후, 앱 종료 이후 시작해야 Notification이 보이는 것 확인
- API level 26 이상에서는 알림을 통해 사용자에게 알리지 않으면 포어그라운드 서비스를 시작할 수 없음
