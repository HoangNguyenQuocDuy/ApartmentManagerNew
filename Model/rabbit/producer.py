import pika
import json
import random
from datetime import datetime

def send_fire_alert(data: dict):
    connection = pika.BlockingConnection(pika.ConnectionParameters(host='localhost'))
    channel = connection.channel()

    channel.exchange_declare(exchange='alertExchange', exchange_type='direct', durable=True)

    camera_id = data.get("cameraId") or f"CAM-{random.randint(1000,9999)}"
    location = data.get("location") or "Unknown"

    payload = {
        "cameraId": camera_id,
        "location": location,
        "timestamp": datetime.now().isoformat()
    }

    channel.basic_publish(
        exchange='alertExchange',
        routing_key='LZwwaGgBmN',
        body=json.dumps(payload),
        properties=pika.BasicProperties(content_type='application/json')
    )

    print("🔥 Đã gửi alert:", payload)
    connection.close()
    