
UnJar:
$ tar -xvf PA5.tar
$ cd src

Compile EVERYTHING with this command:
$ javac -cp .:../lib/* com/graphics/main/Raytracer.java

Run each scene:
$ java -cp .:../lib/* com.graphics.main.Raytracer <Input scene file>  <output filename>

Run Masterwork scene:

$ java -cp .:../lib/* com.graphics.main.Raytracer


Output goes to whatever the name of the file is, and same directory.
Masterwork file goes to masterwork.ppm

EXAMPLE scene1:
$ java -cp .:../lib/* com.graphics.main.Raytracer scene1.txt scene1.ppm

