/*
     Servo Motor Control using Arduino and PCA9685 Driver
           by Dejan, https://howtomechatronics.com
           
     Library: https://github.com/NachtRaveVL/PCA9685-Arduino
*/

#include "ROT3U.h"

#undef DEBUG
#undef DEBUG_ADVANCED

#define CMD_TEST_RESPONSE "TR"
#define CMD_SHOULDER_MOVE_HORIZONTALLY "SH"
#define CMD_SHOULDER_MOVE_VERTICALLY "SV"
#define CMD_ELBOW_MOVE_VERTICALLY "EV"
#define CMD_WRIST_ROTATE "WR"
#define CMD_WRIST_MOVE_VERTICALLY "WV"
#define CMD_CLAMP_OPEN "CO"
#define CMD_CLAMP_CLOSE "CC"
#define CMD_CLAMP_SET "CS"
#define CMD_MOVE_TO_DEFAULT_POSITION "DP"
#define CMD_MOVE_YAKULT "MY"
#define CMD_MOVE_QUEEN "MQ"

#define ACKNOWLEDGE "ACK"

#define ERROR_NO_COMMAND_FOUND "No command found."
#define ERROR_INVALID_DEGREES "Invalid degrees."
#define ERROR_SPACE_OR_OPEN_BRAKET_MISSING "Space or open braket missing."
#define ERROR_NO_ID_FOUND "No id found."
#define ERROR_NO_CLOSING_BRAKET "No closing braket."

#define ACTION_VALID_CHARS "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
#define SPACES " \t"
#define DIGITS "0123456789"
#define ID_OPEN '['
#define ID_CLOSE ']'

#define CHAR_SPACE 0x20
#define CHAR_LF 0x0a
#define CHAR_MINUS '-'

#define END_COMMAND CHAR_LF

char inChar;
String buffer = "", command, val, id;
bool done = false;
ROT3U* pArm;
float deg;

void setup() {
  Serial.begin(9600);
  while (!Serial) {
    ; // wait for serial port to connect. Needed for native USB port only
  }
  Serial.setTimeout(10);
  Serial.println("Starting the arm. Please stand away to a safe position while the arm moves to its default position.");

  pArm = new ROT3U();
  command = "";
  val = "";
}

void loop() {
  while (Serial.available() > 0) {
    int inChar = Serial.read();
/*
 * SH - Shoulder Horizontal > 0 LHS, < 0 RHS
 * SV - Shoulder Vertical > 0 backward, < 0 forward
 * EV - Elbow Vertical < 0 backward, > 0 forward
 * WR - Wrist Rotate < 0 counterclockwise, > 0 clockwise
 * WV - Wrist Vertical < 0 backword, > 0 forward
 * CO - Clamp Open
 * CC - Clam Closed
 * CS - Clamp Set < 0, > 0
 * DP - Default Position
 * MY - Move Yakult
 */
    if (inChar == END_COMMAND) {
      if (parseCommand(buffer)) {
        if (command.compareTo(CMD_SHOULDER_MOVE_HORIZONTALLY) == 0) {
          Serial.println(CMD_SHOULDER_MOVE_HORIZONTALLY);
          pArm->moveShoulderHorizontally(deg);
        } else if (command.compareTo(CMD_SHOULDER_MOVE_VERTICALLY) == 0) {
          Serial.println(CMD_SHOULDER_MOVE_VERTICALLY);
          pArm->moveShoulderVertically(deg);
        } else if (command.compareTo(CMD_ELBOW_MOVE_VERTICALLY) == 0) {
          Serial.println(CMD_ELBOW_MOVE_VERTICALLY);
          pArm->moveElbowVertically(deg);
        } else if (command.compareTo(CMD_WRIST_MOVE_VERTICALLY) == 0) {
          Serial.println(CMD_WRIST_MOVE_VERTICALLY);
          pArm->moveWristVertically(deg);
        } else if (command.compareTo(CMD_WRIST_ROTATE) == 0) {
          Serial.println(CMD_WRIST_ROTATE);
          pArm->rotateWrist(deg);
        } else if (command.compareTo(CMD_CLAMP_OPEN) == 0) {
          Serial.println(CMD_CLAMP_OPEN);
          pArm->openClamp();
        } else if (command.compareTo(CMD_CLAMP_CLOSE) == 0) {
          Serial.println(CMD_CLAMP_CLOSE);
          pArm->closeClamp();
        } else if (command.compareTo(CMD_CLAMP_SET) == 0) {
          Serial.println(CMD_CLAMP_SET);
          pArm->setClamp(deg);
        } else if (command.compareTo(CMD_MOVE_YAKULT) == 0) {
          Serial.println(CMD_MOVE_YAKULT);
          moveYakult();
        } else if (command.compareTo(CMD_MOVE_QUEEN) == 0) {
          Serial.println(CMD_MOVE_QUEEN);
          moveQueen();
        } else if (command.compareTo(CMD_MOVE_TO_DEFAULT_POSITION) == 0) {
          Serial.println(CMD_MOVE_TO_DEFAULT_POSITION);
          moveToDefaultPosition();
        } else {
          Serial.print("unknown command: ");
          Serial.println(command);
        }
#ifdef DEBUG_ADVANCED
        Serial.print("Loop Command: ");
        Serial.println(command);

        Serial.print("Loop Degrees: ");
        Serial.println(deg);
        
        Serial.print("Loop id: ");
        Serial.println(id);
#endif  //  DEBUG_ADVANCED
        Serial.print(ACKNOWLEDGE);
        Serial.print((char)CHAR_SPACE);
        Serial.print(ID_OPEN);
        Serial.print(id);
        Serial.println(ID_CLOSE);
      }
      buffer = "";
    } else {
      buffer += (char)inChar;
    }
  }
}

/**
 * Parse a command that should be in the following RegEx form:
 * 
 * ^[ \t]*[A-Z]+-?\d*\s\[\d+]$
 * 
 * which should look like
 * 
 * <command><space>[<command id>]
 * 
 * and where <command> is:
 * 
 * <action><[-]value>
 * 
 * where action is one of the following:
 * 
 * SH - Shoulder Horizontal
 * SV - Shoulder Vertical 
 * EV - Elbow Vertical
 * WR - Wrist Rotate
 * WV - Wrist Vertical
 * CO - Clamp Open
 * CC - Clam Close
 * CS - Clamp Set
 * DP - Set the arm to the default Position
 * MY - Move a Yakult jar
 * 
 * and the value may or may not have a leading minus sign.
 * 
 * Valid data:
 *     SV-20 [123456]
 * WR50 [12345]
 * 
 *   WR [1234567]
 * WV [12345678]
 * 
 * In the command global variable will put the arm command itself (e.g. SV-20, WR50, etc.)
 * In the id global variable will put the command id 
 * 
 * Ideally, the id represents the computer's milloseconds with appended an incremental number.  
 */
bool parseCommand(String data) {
  String degrees = "";
  bool valid = true;
  bool minus = false;
  unsigned int start, end, len = data.length();
  command = "";
  id = "";

#ifdef DEBUG_ADVANCED
  Serial.print("parseCommand(");
  Serial.print(data);
  Serial.println(")");
#endif  //  DEBUG_ADVANCED
  
  //  Skip initial spaces
  for (start = 0; start < len; start++) {
    if (strchr(SPACES, data.charAt(start)) == nullptr) {
      break; 
    }
  }

  //  Get the action, usually a couple of uppercase letters
  for(; start < len; start++) {
    inChar = data.charAt(start);
    if (strchr(ACTION_VALID_CHARS, inChar) == nullptr) {
      break;
    }
    command.concat((char)data.charAt(start));
  }

  if (command.length() > 0) {
    //  Check whether there is a minus '-' char
    if (inChar == CHAR_MINUS) {
      minus = true;
      start++;
    }
    
    //  Get the degrees, if any
    for(; start < len; start++) {
      inChar = data.charAt(start);
      if (strchr(DIGITS, inChar) == nullptr) {
        break;
      }
      degrees += (char)data.charAt(start);
    }

    if (!minus || degrees.length() > 0) {
      String dgs = "";
      if (minus) {
        dgs = "-";
      }
      dgs += degrees;
#ifdef DEBUG_ADVANCED
      Serial.print("parseCommand degrees: ");
      Serial.println(dgs);
#endif  //  DEBUG_ADVANCED
      deg = dgs.toInt();

      //  Get the id. Before the id there must be a space and an open square braket.
      if (start+2 < len && strchr(SPACES, data.charAt(start)) != nullptr && data.charAt(start+1) == ID_OPEN) {
        //  Get all digits
        for(start += 2 ; start < len; start++) {
          inChar = data.charAt(start);
          if (strchr(DIGITS, inChar) != nullptr) {
            id += (char)inChar;
          } else {
#ifdef DEBUG_ADVANCED      
            Serial.print("parseCommand id: ");
            Serial.println(id);
#endif  //  DEBUG_ADVANCED
            break;
          }
        }
        if (id.length() > 0) {
          if (data.charAt(start) != ID_CLOSE) {
            Serial.println(ERROR_NO_CLOSING_BRAKET);
            valid = false;
          }
        } else {
            Serial.println(ERROR_NO_ID_FOUND);
            valid = false;
        }
      } else {
        Serial.println(ERROR_SPACE_OR_OPEN_BRAKET_MISSING);
        valid = false;
      }
    } else {
      Serial.println(ERROR_INVALID_DEGREES);
      valid = false;
    }
  } else {
    Serial.println(ERROR_NO_COMMAND_FOUND);
    valid = false;
  }

  return valid;
}

void oldLoop() {
  while (Serial.available() > 0) {
    int inChar = Serial.read();
/*
 * SH - Shoulder Horizontal > 0 LHS, < 0 RHS
 * SV - Shoulder Vertical > 0 backward, < 0 forward
 * EV - Elbow Vertical < 0 backward, > 0 forward
 * WR - Wrist Rotate < 0 counterclockwise, > 0 clockwise
 * WV - Wrist Vertical < 0 backword, > 0 forward
 * CO - Clamp Open
 * CC - Clam Closed
 * CS - Clamp Set < 0, > 0
 * DP - Default Position
 * MY - Move Yakult
 */
    if (inChar == END_COMMAND) {
      Serial.println("end command received");
      if (command.length() == 2) {
//        Serial.print("command: received: ");
//        Serial.println(command);
        float deg;
        if (val.length() > 0) {
          deg = val.toInt();
        } else {
          deg = 0.0f;
        }
//        Serial.print("degrees: received: ");
        Serial.println(deg);
        if (command.compareTo(CMD_SHOULDER_MOVE_HORIZONTALLY) == 0) {
          Serial.println(CMD_SHOULDER_MOVE_HORIZONTALLY);
          pArm->moveShoulderHorizontally(deg);
        } else if (command.compareTo(CMD_SHOULDER_MOVE_VERTICALLY) == 0) {
          Serial.println(CMD_SHOULDER_MOVE_VERTICALLY);
          pArm->moveShoulderVertically(deg);
        } else if (command.compareTo(CMD_ELBOW_MOVE_VERTICALLY) == 0) {
          Serial.println(CMD_ELBOW_MOVE_VERTICALLY);
          pArm->moveElbowVertically(deg);
        } else if (command.compareTo(CMD_WRIST_MOVE_VERTICALLY) == 0) {
          Serial.println(CMD_WRIST_MOVE_VERTICALLY);
          pArm->moveWristVertically(deg);
        } else if (command.compareTo(CMD_WRIST_ROTATE) == 0) {
          Serial.println(CMD_WRIST_ROTATE);
          pArm->rotateWrist(deg);
        } else if (command.compareTo(CMD_CLAMP_OPEN) == 0) {
          Serial.println(CMD_CLAMP_OPEN);
          pArm->openClamp();
        } else if (command.compareTo(CMD_CLAMP_CLOSE) == 0) {
          Serial.println(CMD_CLAMP_CLOSE);
          pArm->closeClamp();
        } else if (command.compareTo(CMD_CLAMP_SET) == 0) {
          Serial.println(CMD_CLAMP_SET);
          pArm->setClamp(deg);
        } else if (command.compareTo(CMD_MOVE_YAKULT) == 0) {
          Serial.println(CMD_MOVE_YAKULT);
          moveYakult();
        } else if (command.compareTo(CMD_MOVE_QUEEN) == 0) {
          Serial.println(CMD_MOVE_QUEEN);
          moveQueen();
        } else if (command.compareTo(CMD_MOVE_TO_DEFAULT_POSITION) == 0) {
          Serial.println(CMD_MOVE_TO_DEFAULT_POSITION);
          moveToDefaultPosition();
        } else {
          Serial.print("unknown command: ");
          Serial.println(command);
        }
      }
      val = "";
    } else if (command.length() < 2) {
      Serial.print("received char: ");
      Serial.println(inChar);
      
      command += (char)inChar;
      Serial.print("current command: ");
      Serial.println(command);
    } else if (isDigit(inChar) || inChar == CHAR_MINUS) {
      val += (char)inChar;
    }
  }
}

void moveToDefaultPosition() {
  pArm->moveShoulderHorizontally(0.0f);
  delay(500);
  pArm->moveShoulderVertically(0.0f);
  delay(500);
  pArm->moveElbowVertically(0.0f);
  delay(500);
  pArm->rotateWrist(-90.0f);
  delay(500);
  pArm->moveWristVertically(0.0f);
  delay(500);
  pArm->setClamp(0.0f);
  delay(500);
  pArm->moveWristVertically(-90.0f);
  delay(500);
  pArm->moveElbowVertically(-90.0f);
  delay(500);
  pArm->moveShoulderVertically(-90.0f);
  delay(500);
  pArm->moveElbowVertically(85.0f);
}

void moveYakult() {
//  moveToDefaultPosition();
  pArm->openClamp();
  pArm->rotateWrist(-90.0f);
  pArm->moveShoulderVertically(0.0f);
  delay(500);
  pArm->moveShoulderHorizontally(0.0f);
  delay(500);
  pArm->moveShoulderVertically(-90.0f);  
  delay(500);
  pArm->moveElbowVertically(0.0f);
  delay(500);
  pArm->moveWristVertically(45.0f);
  delay(500);
  pArm->setClamp(70.0f);
  delay(500);
  pArm->moveWristVertically(0.0f);
  delay(500);
  pArm->moveShoulderHorizontally(-45.0f);
  delay(500);
  pArm->moveWristVertically(45.0f);
  delay(500);
  pArm->openClamp();
  pArm->moveShoulderVertically(0.0f);
}

void moveQueen() {
//  moveToDefaultPosition();
  pArm->openClamp();
  pArm->rotateWrist(-90.0f);
  pArm->moveShoulderVertically(0.0f);
  delay(500);
  pArm->moveShoulderHorizontally(0.0f);
  delay(500);
  pArm->moveShoulderVertically(-90.0f);  
  delay(500);
  pArm->moveElbowVertically(0.0f);
  delay(500);
  pArm->moveWristVertically(32.0f);
  delay(500);
  pArm->setClamp(90.0f);
  delay(500);
  pArm->moveWristVertically(0.0f);
  delay(500);
  pArm->moveShoulderHorizontally(-45.0f);
  delay(500);
  pArm->moveWristVertically(45.0f);
  delay(500);
  pArm->openClamp();
  pArm->moveShoulderVertically(0.0f);
}
