#ifndef ROT3U_h
  #define ROT3U_h

#define NUM_OF_SERVOS 6

#define SERVO_SHOULDER_X 0
#define SERVO_SHOULDER_Y 1
#define SERVO_ELBOW 2
#define SERVO_WRIST_X 4
#define SERVO_WRIST_Y 3
#define SERVO_CLAMP 5

#define CLAMP_CLOSE 90.0f
#define CLAMP_MIN 20.0f
#define CLAMP_OPEN CLAMP_MIN

#define DEFAULT_MOVE_DELAY 10

#undef DEBUG

#define CLAMP 5
#define CLAMP_MIN 20.0f

class ROT3U {
  float* pLastServoAngles;

  void initialise();
  void move(int channel, float from, float to);

public:

  ROT3U();
  virtual ~ROT3U();

  void moveShoulderVertically(float degrees);
  void moveShoulderHorizontally(float degrees);
  void moveElbowVertically(float degrees);
  void moveWristVertically(float degrees);
  void rotateWrist(float degrees);
  void closeClamp();
  void openClamp();
  void setClamp(float degrees);

  static bool isValid(float degrees);
};
#endif  // ROT3U_h
