from ultralytics import YOLO
from rabbit.producer import send_fire_alert
from datetime import datetime
import time

model = YOLO("fire_model/fire_detection_model.pt")

CONFIDENCE_THRESHOLD = 0.85       # Ngưỡng độ tin cậy
REQUIRED_DURATION = 3            # Số giây phải phát hiện liên tục

fire_start_time = None            # Thời gian bắt đầu thấy cháy

def detect_fire():
    global fire_start_time

    results = model.predict(source=0, imgsz=640, conf=0.6, stream=True, show=True)

    for r in results:
        fire_detected = False

        for box in r.boxes:
            cls = int(box.cls[0])
            label = model.names[cls]
            confidence = float(box.conf[0])  # Độ tin cậy
            timestamp = datetime.now().strftime("%Y-%m-%d %H:%M:%S")

            if label.startswith("fire"):
                print(f"[{timestamp}] 🔥 Detected '{label}' with confidence: {confidence:.2f}")

                if confidence >= CONFIDENCE_THRESHOLD:
                    fire_detected = True
                else:
                    print(f"⚠️ Bỏ qua vì độ tin cậy thấp hơn ngưỡng {CONFIDENCE_THRESHOLD}")

        current_time = time.time()

        if fire_detected:
            if fire_start_time is None:
                fire_start_time = current_time
                print("⏱️ Bắt đầu tính thời gian phát hiện cháy...")
            elif current_time - fire_start_time >= REQUIRED_DURATION:
                send_fire_alert({
                    "location": "Lobby",
                    "time": datetime.now().isoformat()
                })
                print("🚨 ĐÃ GỬI CẢNH BÁO CHÁY")
                fire_start_time = None  # Reset
        else:
            fire_start_time = None  # Reset nếu không thấy cháy

        time.sleep(0.1)

if __name__ == "__main__":
    detect_fire()
