
UnJar:
$ tar -xvf PA3.tar
$ cd src

Compile EVERYTHING with this command:
$ javac -cp .:../lib/* com/graphics/main/Raytracer.java

Run:
$ java -cp .:../lib/* com.graphics.main.Raytracer <input camera file>  <input PLY file> <output filename>


Output goes to whatever the name of the file is, and same directory.


EXAMPLE:
$ java -cp .:../lib/* com.graphics.main.Raytracer ../assets/examples/ellelltri_cam01.txt ../assets/examples/ellelltri.ply ../assets/ellelltri.ppm

