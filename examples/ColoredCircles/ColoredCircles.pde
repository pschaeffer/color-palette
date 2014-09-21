import colorpalette.*;
/* Define a Color Palette reference */
ColorPalette colorPalette;
/* Standard Processing setup routine */
void setup() {
  size(400,400);
  background(0);
  /* Create an instance of the Color Palette class */  
  colorPalette = new ColorPalette();
  /* Remove Black from the color pool so that it won't be
     returned below */ 
  colorPalette.getName("Black");
}
/* Standard Processing draw routine */
void draw() {
  delay(500);
  /* Check if we have any colors left */
  if (colorPalette.colorsLeft() == 0)
    return;
  /* Get the next free color from the color pool */
  color y = colorPalette.getNext();  
  println(String.format("0x%08x", y));
  /* Draw the circle */
  fill(y);
  ellipse(200, 200, 100, 100);  
}

