// ROT3U.cpp : This file contains the 'main' function. Program execution begins and ends there.
//

#include "ROT3U.h"
#include <Wire.h>
#include "PCA9685.h"

PCA9685 driver;

// PCA9685 outputs = 12-bit = 4096 steps
// 2.5% of 20ms = 0.5ms ; 12.5% of 20ms = 2.5ms
// 2.5% of 4096 = 102 steps; 12.5% of 4096 = 512 steps
PCA9685_ServoEvaluator pwmServo(102, 470); // (-90deg, +90deg)

ROT3U::ROT3U() : pLastServoAngles(nullptr) {
  initialise();
}

ROT3U::~ROT3U() {
  delete[] pLastServoAngles;
  pLastServoAngles = nullptr;
}

void ROT3U::initialise() {

  Wire.begin();                 // Wire must be started first
  Wire.setClock(400000);        // Supported baud rates are 100kHz, 400kHz, and 1000kHz
  driver.resetDevices();        // Software resets all PCA9685 devices on Wire line

  driver.init(B000000);         // Address pins A5-A0 set to B000000
  driver.setPWMFrequency(50);   // Set frequency to 50Hz

  if (pLastServoAngles != nullptr) {
    delete[] pLastServoAngles;
  }
  pLastServoAngles = new float[NUM_OF_SERVOS];
  for (int i = 0; i < NUM_OF_SERVOS; i++) {
    pLastServoAngles[i] = 0.0f;
  }
  pLastServoAngles[SERVO_CLAMP] = CLAMP_MIN;
}

void ROT3U::moveShoulderVertically(float degrees) {
  move(SERVO_SHOULDER_Y, pLastServoAngles[SERVO_SHOULDER_Y], degrees);
  pLastServoAngles[SERVO_SHOULDER_Y] = degrees;
}

void ROT3U::moveShoulderHorizontally(float degrees) {
  move(SERVO_SHOULDER_X, pLastServoAngles[SERVO_SHOULDER_X], degrees);
  pLastServoAngles[SERVO_SHOULDER_X] = degrees;
}

void ROT3U::moveElbowVertically(float degrees) {
  move(SERVO_ELBOW, pLastServoAngles[SERVO_ELBOW], degrees);
  pLastServoAngles[SERVO_ELBOW] = degrees;
}

void ROT3U::moveWristVertically(float degrees) {
  move(SERVO_WRIST_Y, pLastServoAngles[SERVO_WRIST_Y], degrees);
  pLastServoAngles[SERVO_WRIST_Y] = degrees;
}

void ROT3U::rotateWrist(float degrees) {
  move(SERVO_WRIST_X, pLastServoAngles[SERVO_WRIST_X], degrees);
  pLastServoAngles[SERVO_WRIST_X] = degrees;
}

void ROT3U::closeClamp() {
//  driver.setChannelPWM(SERVO_CLAMP, CLAMP_CLOSE);
//  Serial.print("CLAMP CLOSE ");
//  println(pLastServoAngles[SERVO_CLAMP]);
  move(SERVO_CLAMP, pLastServoAngles[SERVO_CLAMP], CLAMP_CLOSE);
  pLastServoAngles[SERVO_CLAMP] = CLAMP_CLOSE;
}

void ROT3U::openClamp() {
//  driver.setChannelPWM(SERVO_CLAMP, CLAMP_OPEN);
//  Serial.print("CLAMP OPEN ");
//  Serial.println(pLastServoAngles[SERVO_CLAMP]);
  move(SERVO_CLAMP, pLastServoAngles[SERVO_CLAMP], CLAMP_OPEN);
  pLastServoAngles[SERVO_CLAMP] = CLAMP_OPEN;
}

void ROT3U::setClamp(float degrees) {
//  Serial.print("CLAMP OPEN ");
//  Serial.println(pLastServoAngles[SERVO_CLAMP]);
  if (degrees < CLAMP_MIN) {
    degrees = CLAMP_MIN;
  }
  if (degrees > 90.0f) {
    degrees = 90.0f;
  }
  move(SERVO_CLAMP, pLastServoAngles[SERVO_CLAMP], degrees);
  pLastServoAngles[SERVO_CLAMP] = degrees;
}

void ROT3U::move(int channel, float from, float to) {
  if (isValid(from) && isValid(to)) {
    if (to <= -90.0f) {
      to = -90.0f;
    } else if (to >= 90.f) {
      to = 90.0f;
    }
    if (channel == CLAMP) {
      if (from < CLAMP_MIN) {
        from = CLAMP_MIN;
      }
    }
#ifdef DEBUG
    Serial.print("Channel ");
    Serial.print(channel);
    Serial.print(" - from ");
    Serial.print(from);
    Serial.print(" - to ");
    Serial.println(to);
#else //  DEBUG
    if (to > from) {
      for (float i = from; i <= to; i++) {
        delay(DEFAULT_MOVE_DELAY);
        driver.setChannelPWM(channel, pwmServo.pwmForAngle(i));
      }
    } else {
      for (float i = from; i >= to; i--) {
        delay(DEFAULT_MOVE_DELAY);
        driver.setChannelPWM(channel, pwmServo.pwmForAngle(i));
      }
    }
#endif  //  DEBUG
  }
}

static bool ROT3U::isValid(float degrees) {
  if (degrees >= -90.0f && degrees <= 90.0f) {
    return true;
  }
  return false;
}
