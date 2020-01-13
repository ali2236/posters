import 'dart:io';
import 'dart:math';

Random r = Random();
int b = 100000;

void main(List<String> args) async {
  int count = 10000;

  List<Rect> rects = List.generate(count, (i) {
    var x2 = r.nextInt(b);
    var y2 = r.nextInt(b);
    return Rect(
      r.nextInt(x2),
      r.nextInt(y2),
      x2,
      y2,
    );
  });

  var testFile = File("test.txt");
  if(testFile.existsSync()){
    testFile.deleteSync();
  }
  await testFile.create(recursive: true);
  var sink = testFile.openWrite();

  sink.writeln(count);
  rects.forEach((r)=>sink.writeln(r.toString()));
  await sink.flush();
  await sink.close();
}

class Rect {
  final int x1, y1, x2, y2;
  Rect(this.x1, this.y1, this.x2, this.y2);
  @override
  String toString() => "$x1 $y1 $x2 $y2";
}
