import colorpalette.*;
/* Define a Color Palette reference */
ColorPalette colorPalette;
/* Standard Processing setup routine */
void setup() {
  color    a, b, c;
  Integer  d;
  size(400,400);
  background(0);
  /* Create an instance of the Color Palette class */  
  colorPalette = new ColorPalette();
  /* Print the number of available colors */
  println("Number of colors left -", colorPalette.colorsLeft()); 
  /* Get and return a few colors from the color pool */
  b = colorPalette.getName("Black");
  println("Got Black from the pool");
  /* Try to get Black from the color pool again. Note that the return
     value is specified as an Integer because the return value will be
     null in this case */
  d = colorPalette.getName("Black");
  if (d == null)
    println("Black is not available");
  else
    println("Black is available");
  /* Put Black back in the color pool */
  colorPalette.putBack(b);  
  /* Show that Black is now available */
  d = colorPalette.getName("Black");
  if (d == null)
    println("Black is not available");
  else
    println("Black is available");
  /* Remove and return some colors from/to the color pool */
  println("Number of colors left -", colorPalette.colorsLeft());  
  a = colorPalette.getNext();
  println("Number of colors left -", colorPalette.colorsLeft());  
  colorPalette.putBack(a);
  println("Number of colors left -", colorPalette.colorsLeft());  
  b = colorPalette.getNext();
  c = colorPalette.getNext(); 
  println("Number of colors left -", colorPalette.colorsLeft());     
}
/* Standard Processing draw routine */
void draw() { 
}

   
