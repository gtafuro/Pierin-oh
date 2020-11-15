from tkinter import *
import math, os

width = 800
height = 512

xc = width/2
yc = height/2

baseHeight = 135

angle = 90
shoulderLength = 105
shoulderX = 0
shoulderY = 0

angle1 = 90
elbowLength = 100
elbowX = 0
elbowY = 0

angle2 = -90
clampLength = 155
wristX = 0
wristY = 0

sec = 0

readKey = True

def down(e):
    global arm, readKey, angle, angle1, angle2, baseHeight
    
    if readKey:
        readKey = False
#        print('Down\n', e.char, '\n', e)
        if e.char == 'q':
            shDeg = angle-1
            elDeg = angle1
            wrDeg = angle2
        elif e.char == 'w':
            shDeg = 0
            elDeg = angle1
            wrDeg = angle2
        elif e.char == 'e':
            shDeg = angle+1
            elDeg = angle1
            wrDeg = angle2
        elif e.char == 'a':
            shDeg = angle
            elDeg = angle1-1
            wrDeg = angle2
        elif e.char == 's':
            shDeg = angle
            elDeg = 0
            wrDeg = angle2
        elif e.char == 'd':
            shDeg = angle
            elDeg = angle1+1
            wrDeg = angle2
        elif e.char == 'z':
            shDeg = angle
            elDeg = angle1
            wrDeg = angle2-1
        elif e.char == 'x':
            shDeg = angle
            elDeg = angle1
            wrDeg = 0
        elif e.char == 'c':
            shDeg = angle
            elDeg = angle1
            wrDeg = angle2+1
        else:
            shDeg = angle
            elDeg = angle1
            wrDeg = angle2

#        print("Been here, done things")

        totDeg = shDeg+elDeg+wrDeg

        if is_valid_degree(shDeg) and is_valid_degree(elDeg) and is_valid_degree(wrDeg):
            ver = vertical(shDeg, elDeg, wrDeg)
            if ver > 0:
#                clockwise = angle+angle1+angle2 < shDeg+elDeg+wrDeg
                hor = horizontal(shDeg, elDeg, wrDeg)
                prevHor = horizontal(angle, angle1, angle2)
#                if (shDeg < 0 and hor < 0) or (shDeg>=0 and ver >= baseHeight) or (shDeg >= 0 and ver < baseHeight and hor > 0):
                print("----------\nver = "+str(ver)+"\nhor = "+str(hor)+"\nprevHor = "+str(prevHor))
                if ver >= baseHeight or (hor >= 0 and prevHor >= 0) or (hor < 0 and prevHor < 0):
                    if e.char == 'q' or e.char == 'w' or e.char == 'e':
                        angle = shDeg
                    elif e.char == 'a' or e.char == 's' or e.char == 'd':
                        angle1 = elDeg
                    elif e.char == 'z'or e.char == 'x' or e.char == 'c':
                            angle2 = wrDeg
                    pos()
                    arm.paint()
    readKey = True

def is_valid_degree(deg):
    return deg >= -90 and deg <= 90

def pos():
    global shoulderX, shoulderY, elbowX, elbowY, wristX, wristY
    shoulderX = shoulderLength * math.sin(math.radians(angle))
    shoulderY = shoulderLength * math.cos(math.radians(angle))
    elbowX    = elbowLength    * math.sin(math.radians(angle+angle1))
    elbowY    = elbowLength    * math.cos(math.radians(angle+angle1))
    wristX    = clampLength    * math.sin(math.radians(angle+angle1+angle2))
    wristY    = clampLength    * math.cos(math.radians(angle+angle1+angle2))

def translate_coordinates(x, y):
    global height
    return x, height-y

def translateY(y):
    global height
    return height-y

def motion(event):
  print("Mouse position: (%s %s)" % (event.x, event.y))
  return

def horizontal(shDeg, elDeg, wrDeg):
    global angle, shoulderLength, angle1, elbowLength, angle2, clampLength

    shCos = math.sin(math.radians(shDeg));
    elCos = math.sin(math.radians(shDeg+elDeg));
    wrCos = math.sin(math.radians(shDeg+elDeg+wrDeg));

    retval = shoulderLength*shCos+elbowLength*elCos+clampLength*wrCos

    return int(retval*1000)/1000

def vertical(shDeg, elDeg, wrDeg):
    global angle, shoulderLength, angle1, elbowLength, angle2, clampLength

    shCos = math.cos(math.radians(shDeg));
    elCos = math.cos(math.radians(shDeg+elDeg));
    wrCos = math.cos(math.radians(shDeg+elDeg+wrDeg));

    retval = baseHeight+shoulderLength*shCos+elbowLength*elCos+clampLength*wrCos

    return int(retval*1000)/1000

class ROT6U(Frame):
    key = ''
    last_press_time = 0

    def __init__(self, master):
        frame = Frame(master, width=800, height=512, bd=1)
        frame.pack()
        
        self.canvas = Canvas(frame, bg='white', width=800, height=512)
        self.paint()

    def paint(self):
        self.canvas.delete("all")
        self.canvas.create_line(xc,                      translateY(0),                         xc,                               translateY(baseHeight), width=10, fill='yellow')
        self.canvas.create_line(xc,                      translateY(baseHeight),                      xc + shoulderX,                   translateY(baseHeight + shoulderY), width=10, fill='red')
        self.canvas.create_line(xc + shoulderX,          translateY(baseHeight + shoulderY),          xc + shoulderX + elbowX,          translateY(baseHeight + shoulderY + elbowY), width=10, fill='green')
        self.canvas.create_line(xc + shoulderX + elbowX, translateY(baseHeight + shoulderY + elbowY), xc + shoulderX + elbowX + wristX, translateY(baseHeight + shoulderY + elbowY + wristY), width=10, fill='blue')
        self.canvas.pack(fill=BOTH, expand=1)
        self.print_angles()
        
    def print_angles(self):
        global angle, angle1, angle2
        text = "sh ° = "+str(angle)
        self.canvas.create_text(0, 0, anchor="nw", fill="red", font="Helvetica 15 italic bold", text=text)
        text = "el ° = "+str(angle1)
        self.canvas.create_text(0, 20, anchor="nw", fill="green", font="Helvetica 15 italic bold", text=text)
        text = "wr ° = "+str(angle2)
        self.canvas.create_text(0, 40, anchor="nw", fill="blue", font="Helvetica 15 italic bold", text=text)
        text = "total ° = "+str(angle+angle1+angle2)
        self.canvas.create_text(0, 60, anchor="nw", fill="black", font="Helvetica 15 italic bold", text=text)
        text = "hor = "+str(horizontal(angle, angle1, angle2))
        self.canvas.create_text(0, 80, anchor="nw", fill="black", font="Helvetica 15 italic bold", text=text)
        text = "ver = "+str(vertical(angle, angle1, angle2))
        self.canvas.create_text(0, 100, anchor="nw", fill="black", font="Helvetica 15 italic bold", text=text)

def main():
    global arm

    os.system("xset r on")

    pos()

    root = Tk()
    #root.option_add('*font', ('verdana', 10, 'bold'))
    arm = ROT6U(root)
    root.title('Tkinter Widgets')
    root.bind('<KeyPress>', down)
#    root.bind('<KeyRelease>', up)
    root.mainloop()

if __name__ == '__main__':
    main()
