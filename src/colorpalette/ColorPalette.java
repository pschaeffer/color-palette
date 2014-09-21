package colorpalette;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.TreeSet;
/** 
 * The ColorPalette class manages color pools. The basic idea is that the 
 * ColorPalette class maintains a pool of unique colors (no duplicates). 
 * Each color in the pool has a name (from web standards) and an RGB 
 * value. Once a color has been pulled from the pool, it is deemed to
 * be in use (busy) and will not returned again to a caller, until the 
 * original user return the color to the pool.
 * <p> 
 * The colors pulled from the pool and returned to the caller, will be 
 * in ARGB format. However, the alpha channel will always be set to 255. 
 * This can be changed by the caller as need be. When a color is returned 
 * to the color pool (palette), the alpha channel is ignored. The colors 
 * returned by this class appear to work equally well in RGB mode and HSB
 * mode.
 * <p>
 * The ColorPalette class can be instantiated any number of times. The 
 * underlying list of colors is statically defined. However, a copy of 
 * the color list is built for each instance of this class. All of the 
 * data and methods of this class are instance based (save for the initial 
 * static color list). Instance methods are provided for obtaining colors 
 * from the color pool and returning them to the color pool. The methods 
 * of this class are not thread-safe, but could be modified to make them 
 * thread-safe.
 * <p>
 * Generally colors will be pulled from the pool in order. In other words, 
 * the most popular colors (red, green, blue) will be returned first and 
 * less popular colors will be returned later. If all of the colors have 
 * been pulled from the pool, then the pool (the color palette) is empty 
 * and a request for a new color will return a null value. This is not an 
 * error condition and must be checked for.
 * <p>
 * Colors can be pulled from the pool by name. If the name is unknown, 
 * an exception will be thrown. If the name is valid, but the color is 
 * busy (already in use), then a null value will be returned. This is 
 * not an error condition. Note that color names are case sensitive and 
 * mixed case.
 * <p>
 * A typical use case might be providing colors for balls bouncing around 
 * a box. Assume that balls are added randomly and each ball must have a 
 * unique color. Over time, balls get older and eventually die off. Each 
 * time a new ball is added to the box, the get next color routine could 
 * be used to obtain a unique color. Each time a ball dies, the color of 
 * the ball could be returned to the color palette (pool).
 * 
 * @author      ##author##
 * @version     ##library.prettyVersion##     
 * @since       ##date##
 */
/* 
 * ColorPalette  
 *    The ColorPalette class manages color pools.
 *    Updated Today (2 hours ago) by pschaeffer
 *  Introduction
 *    The ColorPalette class manages color pools allowing Processing 
 *    applications (actually any Java application) to obtains colors 
 *    (next free color or a color by name), use them, and then return 
 *    them to the pool.
 *  Details
 *    The ColorPalette class manages color pools. The basic idea is that 
 *    the ColorPalette class maintains a pool of unique colors (no 
 *    duplicates). Each color in the pool has a name (from web standards) 
 *    and an RGB value. Once a color has been pulled from the pool, it 
 *    is deemed to be in use (busy) and will not returned again to a 
 *    caller, until the original user return the color to the pool.
 */
public final class ColorPalette { 
  /* The array below contains all of the colors that go into the pool */
  final private static ColorItem items[] = {new ColorItem("Red", 255, 0, 0),
                                            new ColorItem("Green", 0, 128, 0),
                                            new ColorItem("Blue", 0, 0, 255),
                                            new ColorItem("Cyan", 0, 255, 255),
                                            new ColorItem("Magenta", 255, 0, 255),
                                            new ColorItem("Yellow", 255, 255, 0),
                                            new ColorItem("Maroon", 128, 0, 0),
                                            new ColorItem("Olive", 128, 128, 0),
                                            new ColorItem("Lime", 0, 255, 0),
                                            new ColorItem("Teal", 0, 128, 128),
                                            new ColorItem("Navy", 0, 0, 128),
                                            new ColorItem("Purple", 128, 0, 128),
                                            new ColorItem("White", 255, 255, 255),
                                            new ColorItem("Black", 0, 0, 0),
                                            new ColorItem("Silver", 192, 192, 192),
                                            new ColorItem("Gray", 128, 128, 128),
                                            new ColorItem("AliceBlue", 240, 248, 255),
                                            new ColorItem("AntiqueWhite", 250, 235, 215),
                                            new ColorItem("Aquamarine", 127, 255, 212),
                                            new ColorItem("Azure", 0, 127, 255),
                                            new ColorItem("Beige", 245, 245, 220),
                                            new ColorItem("Bisque", 255, 228, 196),
                                            new ColorItem("BlanchedAlmond", 255, 235, 205),
                                            new ColorItem("BlueViolet", 138, 43, 226),
                                            new ColorItem("Brown", 165, 42, 42),
                                            new ColorItem("BurlyWood", 222, 184, 135),
                                            new ColorItem("CadetBlue", 95, 158, 160),
                                            new ColorItem("Chartreuse", 127, 255, 0),
                                            new ColorItem("Chocolate", 210, 105, 30),
                                            new ColorItem("Coral", 255, 127, 80),
                                            new ColorItem("CornflowerBlue", 100, 149, 237),
                                            new ColorItem("Cornsilk", 255, 248, 220),
                                            new ColorItem("Crimson", 220, 20, 60),
                                            new ColorItem("DarkBlue", 0, 0, 139),
                                            new ColorItem("DarkCyan", 0, 139, 139),
                                            new ColorItem("DarkGoldenRod", 184, 134, 11),
                                            new ColorItem("DarkGray", 169, 169, 169),
                                            new ColorItem("DarkGreen", 0, 100, 0),
                                            new ColorItem("DarkKhaki", 189, 183, 107),
                                            new ColorItem("DarkMagenta", 139, 0, 139),
                                            new ColorItem("DarkOliveGreen", 85, 107, 47),
                                            new ColorItem("DarkOrange", 255, 140, 0),
                                            new ColorItem("DarkOrchid", 153, 50, 204),
                                            new ColorItem("DarkRed", 139, 0, 0),
                                            new ColorItem("DarkSalmon", 233, 150, 122),
                                            new ColorItem("DarkSeaGreen", 143, 188, 143),
                                            new ColorItem("DarkSlateBlue", 72, 61, 139),
                                            new ColorItem("DarkSlateGray", 47, 79, 79),
                                            new ColorItem("DarkTurquoise", 0, 206, 209),
                                            new ColorItem("DarkViolet", 148, 0, 211),
                                            new ColorItem("DeepPink", 255, 20, 147),
                                            new ColorItem("DeepSkyBlue", 0, 191, 255),
                                            new ColorItem("DimGray", 105, 105, 105),
                                            new ColorItem("DodgerBlue", 30, 144, 255),
                                            new ColorItem("FireBrick", 178, 34, 34),
                                            new ColorItem("FloralWhite", 255, 250, 240),
                                            new ColorItem("ForestGreen", 34, 139, 34),
                                            new ColorItem("Gainsboro", 220, 220, 220),
                                            new ColorItem("GhostWhite", 248, 248, 255),
                                            new ColorItem("Gold", 255, 215, 0),
                                            new ColorItem("GoldenRod", 218, 165, 32),
                                            new ColorItem("GreenYellow", 173, 255, 47),
                                            new ColorItem("HoneyDew", 240, 255, 240),
                                            new ColorItem("HotPink", 255, 105, 180),
                                            new ColorItem("IndianRed", 205, 92, 92),
                                            new ColorItem("Indigo", 75, 0, 130),
                                            new ColorItem("Ivory", 255, 255, 240),
                                            new ColorItem("Khaki", 240, 230, 140),
                                            new ColorItem("Lavender", 230, 230, 250),
                                            new ColorItem("LavenderBlush", 255, 240, 245),
                                            new ColorItem("LawnGreen", 124, 252, 0),
                                            new ColorItem("LemonChiffon", 255, 250, 205),
                                            new ColorItem("LightBlue", 173, 216, 230),
                                            new ColorItem("LightCoral", 240, 128, 128),
                                            new ColorItem("LightCyan", 224, 255, 255),
                                            new ColorItem("LightGoldenRodYellow", 250, 250, 210),
                                            new ColorItem("LightGray", 211, 211, 211),
                                            new ColorItem("LightGreen", 144, 238, 144),
                                            new ColorItem("LightPink", 255, 182, 193),
                                            new ColorItem("LightSalmon", 255, 160, 122),
                                            new ColorItem("LightSeaGreen", 32, 178, 170),
                                            new ColorItem("LightSkyBlue", 135, 206, 250),
                                            new ColorItem("LightSlateGray", 119, 136, 153),
                                            new ColorItem("LightSteelBlue", 176, 196, 222),
                                            new ColorItem("LightYellow", 255, 255, 224),
                                            new ColorItem("LimeGreen", 50, 205, 50),
                                            new ColorItem("Linen", 250, 240, 230),
                                            new ColorItem("MediumAquaMarine", 102, 205, 170),
                                            new ColorItem("MediumBlue", 0, 0, 205),
                                            new ColorItem("MediumOrchid", 186, 85, 211),
                                            new ColorItem("MediumPurple", 147, 112, 219),
                                            new ColorItem("MediumSeaGreen", 60, 179, 113),
                                            new ColorItem("MediumSlateBlue", 123, 104, 238),
                                            new ColorItem("MediumSpringGreen", 0, 250, 154),
                                            new ColorItem("MediumTurquoise", 72, 209, 204),
                                            new ColorItem("MediumVioletRed", 199, 21, 133),
                                            new ColorItem("MidnightBlue", 25, 25, 112),
                                            new ColorItem("MintCream", 245, 255, 250),
                                            new ColorItem("MistyRose", 255, 228, 225),
                                            new ColorItem("Moccasin", 255, 228, 181),
                                            new ColorItem("NavajoWhite", 255, 222, 173),
                                            new ColorItem("OldLace", 253, 245, 230),
                                            new ColorItem("OliveDrab", 107, 142, 35),
                                            new ColorItem("Orange", 255, 165, 0),
                                            new ColorItem("OrangeRed", 255, 69, 0),
                                            new ColorItem("Orchid", 218, 112, 214),
                                            new ColorItem("PaleGoldenRod", 238, 232, 170),
                                            new ColorItem("PaleGreen", 152, 251, 152),
                                            new ColorItem("PaleTurquoise", 175, 238, 238),
                                            new ColorItem("PaleVioletRed", 219, 112, 147),
                                            new ColorItem("PapayaWhip", 255, 239, 213),
                                            new ColorItem("PeachPuff", 255, 218, 185),
                                            new ColorItem("Peru", 205, 133, 63),
                                            new ColorItem("Pink", 255, 192, 203),
                                            new ColorItem("Plum", 142, 69, 133),
                                            new ColorItem("PowderBlue", 176, 224, 230),
                                            new ColorItem("RosyBrown", 188, 143, 143),
                                            new ColorItem("RoyalBlue", 65, 105, 225),
                                            new ColorItem("SaddleBrown", 139, 69, 19),
                                            new ColorItem("Salmon", 250, 128, 114),
                                            new ColorItem("SandyBrown", 244, 164, 96),
                                            new ColorItem("SeaGreen", 46, 139, 87),
                                            new ColorItem("SeaShell", 255, 245, 238),
                                            new ColorItem("Sienna", 160, 82, 45),
                                            new ColorItem("SkyBlue", 135, 206, 235),
                                            new ColorItem("SlateBlue", 106, 90, 205),
                                            new ColorItem("SlateGray", 112, 128, 144),
                                            new ColorItem("Snow", 255, 250, 250),
                                            new ColorItem("SpringGreen", 0, 255, 127),
                                            new ColorItem("SteelBlue", 70, 130, 180),
                                            new ColorItem("Tan", 210, 180, 140),
                                            new ColorItem("Thistle", 216, 191, 216),
                                            new ColorItem("Tomato", 255, 99, 71),
                                            new ColorItem("Turquoise", 64, 224, 208),
                                            new ColorItem("Violet", 238, 130, 238),
                                            new ColorItem("Wheat", 245, 222, 179),
                                            new ColorItem("WhiteSmoke", 245, 245, 245),
                                            new ColorItem("YellowGreen", 154, 205, 50),
                                            new ColorItem("Acid green", 176, 191, 26),
                                            new ColorItem("Aero", 124, 185, 232),
                                            new ColorItem("Aero blue", 201, 255, 229),
                                            new ColorItem("African violet", 178, 132, 190),
                                            new ColorItem("Air Force blue (RAF)", 93, 138, 168),
                                            new ColorItem("Air Force blue (USAF)", 0, 48, 143),
                                            new ColorItem("Air superiority blue", 114, 160, 193),
                                            new ColorItem("Alabama crimson", 175, 0, 42),
                                            new ColorItem("Alizarin crimson", 227, 38, 54),
                                            new ColorItem("Alloy orange", 196, 98, 16),
                                            new ColorItem("Almond", 239, 222, 205),
                                            new ColorItem("Amaranth", 229, 43, 80),
                                            new ColorItem("Amaranth pink", 241, 156, 187),
                                            new ColorItem("Amaranth purple", 171, 39, 79),
                                            new ColorItem("Amaranth red", 211, 33, 45),
                                            new ColorItem("Amazon", 59, 122, 87),
                                            new ColorItem("Amber", 255, 191, 0),
                                            new ColorItem("Amber (SAE/ECE)", 255, 126, 0),
                                            new ColorItem("American rose", 255, 3, 62),
                                            new ColorItem("Amethyst", 153, 102, 204),
                                            new ColorItem("Android green", 164, 198, 57),
                                            new ColorItem("Anti-flash white", 242, 243, 244),
                                            new ColorItem("Antique brass", 205, 149, 117),
                                            new ColorItem("Antique bronze", 102, 93, 30),
                                            new ColorItem("Antique fuchsia", 145, 92, 131),
                                            new ColorItem("Antique ruby", 132, 27, 45),
                                            new ColorItem("Apple green", 141, 182, 0),
                                            new ColorItem("Apricot", 251, 206, 177),
                                            new ColorItem("Army green", 75, 83, 32),
                                            new ColorItem("Arsenic", 59, 68, 75),
                                            new ColorItem("Artichoke", 143, 151, 121),
                                            new ColorItem("Arylide yellow", 233, 214, 107),
                                            new ColorItem("Ash grey", 178, 190, 181),
                                            new ColorItem("Asparagus", 135, 169, 107),
                                            new ColorItem("Atomic tangerine", 255, 153, 102),
                                            new ColorItem("Aureolin", 253, 238, 0),
                                            new ColorItem("AuroMetalSaurus", 110, 127, 128),
                                            new ColorItem("Avocado", 86, 130, 3),
                                            new ColorItem("Azure (web color)", 240, 255, 255),
                                            new ColorItem("Azureish white", 219, 233, 244),
                                            new ColorItem("Baby blue", 137, 207, 240),
                                            new ColorItem("Baby blue eyes", 161, 202, 241),
                                            new ColorItem("Baby pink", 244, 194, 194),
                                            new ColorItem("Baby powder", 254, 254, 250),
                                            new ColorItem("Baker-Miller pink", 255, 145, 175),
                                            new ColorItem("Ball blue", 33, 171, 205),
                                            new ColorItem("Banana Mania", 250, 231, 181),
                                            new ColorItem("Banana yellow", 255, 225, 53),
                                            new ColorItem("Bangladesh green", 0, 106, 78),
                                            new ColorItem("Barbie pink", 224, 33, 138),
                                            new ColorItem("Barn red", 124, 10, 2),
                                            new ColorItem("Battleship grey", 132, 132, 130),
                                            new ColorItem("Bazaar", 152, 119, 123),
                                            new ColorItem("Beau blue", 188, 212, 230),
                                            new ColorItem("Beaver", 159, 129, 112),
                                            new ColorItem("B'dazzled blue", 46, 88, 148),
                                            new ColorItem("Big dip o’ruby", 156, 37, 66),
                                            new ColorItem("Bistre", 61, 43, 31),
                                            new ColorItem("Bistre brown", 150, 113, 23),
                                            new ColorItem("Bitter lemon", 202, 224, 13),
                                            new ColorItem("Bitter lime", 191, 255, 0),
                                            new ColorItem("Bittersweet", 254, 111, 94),
                                            new ColorItem("Bittersweet shimmer", 191, 79, 81),
                                            new ColorItem("Black bean", 61, 12, 2),
                                            new ColorItem("Black leather jacket", 37, 53, 41),
                                            new ColorItem("Black olive", 59, 60, 54),
                                            new ColorItem("Blast-off bronze", 165, 113, 100),
                                            new ColorItem("Bleu de France", 49, 140, 231),
                                            new ColorItem("Blizzard Blue", 172, 229, 238),
                                            new ColorItem("Blond", 250, 240, 190),
                                            new ColorItem("Blue (Crayola)", 31, 117, 254),
                                            new ColorItem("Blue (Munsell)", 0, 147, 175),
                                            new ColorItem("Blue (NCS)", 0, 135, 189),
                                            new ColorItem("Blue (Pantone)", 0, 24, 168),
                                            new ColorItem("Blue (pigment)", 51, 51, 153),
                                            new ColorItem("Blue (RYB)", 2, 71, 254),
                                            new ColorItem("Blue Bell", 162, 162, 208),
                                            new ColorItem("Blue-gray", 102, 153, 204),
                                            new ColorItem("Blue-green", 13, 152, 186),
                                            new ColorItem("Blue-magenta violet", 85, 53, 146),
                                            new ColorItem("Blue sapphire", 18, 97, 128),
                                            new ColorItem("Blue yonder", 80, 114, 167),
                                            new ColorItem("Blueberry", 79, 134, 247),
                                            new ColorItem("Bluebonnet", 28, 28, 240),
                                            new ColorItem("Blush", 222, 93, 131),
                                            new ColorItem("Bole", 121, 68, 59),
                                            new ColorItem("Bondi blue", 0, 149, 182),
                                            new ColorItem("Bone", 227, 218, 201),
                                            new ColorItem("Boston University Red", 204, 0, 0),
                                            new ColorItem("Boysenberry", 135, 50, 96),
                                            new ColorItem("Brandeis blue", 0, 112, 255),
                                            new ColorItem("Brass", 181, 166, 66),
                                            new ColorItem("Brick red", 203, 65, 84),
                                            new ColorItem("Bright cerulean", 29, 172, 214),
                                            new ColorItem("Bright green", 102, 255, 0),
                                            new ColorItem("Bright lavender", 191, 148, 228),
                                            new ColorItem("Bright lilac", 216, 145, 239),
                                            new ColorItem("Bright maroon", 195, 33, 72),
                                            new ColorItem("Bright navy blue", 25, 116, 210),
                                            new ColorItem("Bright pink", 255, 0, 127),
                                            new ColorItem("Bright turquoise", 8, 232, 222),
                                            new ColorItem("Bright ube", 209, 159, 232),
                                            new ColorItem("Brilliant azure", 51, 153, 255),
                                            new ColorItem("Brilliant lavender", 244, 187, 255),
                                            new ColorItem("Brilliant rose", 255, 85, 163),
                                            new ColorItem("Brink pink", 251, 96, 127),
                                            new ColorItem("British racing green", 0, 66, 37),
                                            new ColorItem("Bronze", 205, 127, 50),
                                            new ColorItem("Bronze Yellow", 115, 112, 0),
                                            new ColorItem("Brown (traditional)", 150, 75, 0),
                                            new ColorItem("Brown-nose", 107, 68, 35),
                                            new ColorItem("Brown Yellow", 204, 153, 102),
                                            new ColorItem("Brunswick green", 27, 77, 62),
                                            new ColorItem("Bubble gum", 255, 193, 204),
                                            new ColorItem("Bubbles", 231, 254, 255),
                                            new ColorItem("Buff", 240, 220, 130),
                                            new ColorItem("Bud green", 123, 182, 97),
                                            new ColorItem("Bulgarian rose", 72, 6, 7),
                                            new ColorItem("Burgundy", 128, 0, 32),
                                            new ColorItem("Burnt orange", 204, 85, 0),
                                            new ColorItem("Burnt sienna", 233, 116, 81),
                                            new ColorItem("Burnt umber", 138, 51, 36),
                                            new ColorItem("Byzantine", 189, 51, 164),
                                            new ColorItem("Byzantium", 112, 41, 99),
                                            new ColorItem("Cadet", 83, 104, 114),
                                            new ColorItem("Cadet grey", 145, 163, 176),
                                            new ColorItem("Cadmium green", 0, 107, 60),
                                            new ColorItem("Cadmium orange", 237, 135, 45),
                                            new ColorItem("Cadmium red", 227, 0, 34),
                                            new ColorItem("Cadmium yellow", 255, 246, 0),
                                            new ColorItem("Café au lait", 166, 123, 91),
                                            new ColorItem("Café noir", 75, 54, 33),
                                            new ColorItem("Cal Poly green", 30, 77, 43),
                                            new ColorItem("Cambridge Blue", 163, 193, 173),
                                            new ColorItem("Camel", 193, 154, 107),
                                            new ColorItem("Cameo pink", 239, 187, 204),
                                            new ColorItem("Camouflage green", 120, 134, 107),
                                            new ColorItem("Canary yellow", 255, 239, 0),
                                            new ColorItem("Candy apple red", 255, 8, 0),
                                            new ColorItem("Candy pink", 228, 113, 122),
                                            new ColorItem("Caput mortuum", 89, 39, 32),
                                            new ColorItem("Cardinal", 196, 30, 58),
                                            new ColorItem("Caribbean green", 0, 204, 153),
                                            new ColorItem("Carmine", 150, 0, 24),
                                            new ColorItem("Carmine (M&P)", 215, 0, 64),
                                            new ColorItem("Carmine pink", 235, 76, 66),
                                            new ColorItem("Carmine red", 255, 0, 56),
                                            new ColorItem("Carnation pink", 255, 166, 201),
                                            new ColorItem("Carnelian", 179, 27, 27),
                                            new ColorItem("Carolina blue", 86, 160, 211),
                                            new ColorItem("Carrot orange", 237, 145, 33),
                                            new ColorItem("Castleton green", 0, 86, 63),
                                            new ColorItem("Catalina blue", 6, 42, 120),
                                            new ColorItem("Catawba", 112, 54, 66),
                                            new ColorItem("Cedar Chest", 201, 90, 73),
                                            new ColorItem("Ceil", 146, 161, 207),
                                            new ColorItem("Celadon", 172, 225, 175),
                                            new ColorItem("Celadon blue", 0, 123, 167),
                                            new ColorItem("Celadon green", 47, 132, 124),
                                            new ColorItem("Celeste", 178, 255, 255),
                                            new ColorItem("Celestial blue", 73, 151, 208),
                                            new ColorItem("Cerise", 222, 49, 99),
                                            new ColorItem("Cerise pink", 236, 59, 131),
                                            new ColorItem("Cerulean blue", 42, 82, 190),
                                            new ColorItem("Cerulean frost", 109, 155, 195),
                                            new ColorItem("CG Blue", 0, 122, 165),
                                            new ColorItem("CG Red", 224, 60, 49),
                                            new ColorItem("Chamoisee", 160, 120, 90),
                                            new ColorItem("Champagne", 247, 231, 206),
                                            new ColorItem("Charcoal", 54, 69, 79),
                                            new ColorItem("Charleston green", 35, 43, 43),
                                            new ColorItem("Charm pink", 230, 143, 172),
                                            new ColorItem("Chartreuse (traditional)", 223, 255, 0),
                                            new ColorItem("Cherry blossom pink", 255, 183, 197),
                                            new ColorItem("Chestnut", 149, 69, 53),
                                            new ColorItem("China pink", 222, 111, 161),
                                            new ColorItem("China rose", 168, 81, 110),
                                            new ColorItem("Chinese red", 170, 56, 30),
                                            new ColorItem("Chinese violet", 133, 96, 136),
                                            new ColorItem("Chocolate (traditional)", 123, 63, 0),
                                            new ColorItem("Chrome yellow", 255, 167, 0),
                                            new ColorItem("Cinereous", 152, 129, 123),
                                            new ColorItem("Cinnabar", 227, 66, 52),
                                            new ColorItem("Citrine", 228, 208, 10),
                                            new ColorItem("Citron", 159, 169, 31),
                                            new ColorItem("Claret", 127, 23, 52),
                                            new ColorItem("Classic rose", 251, 204, 231),
                                            new ColorItem("Cobalt blue", 0, 71, 171),
                                            new ColorItem("Coconut", 150, 90, 62),
                                            new ColorItem("Coffee", 111, 78, 55),
                                            new ColorItem("Columbia blue", 196, 216, 226),
                                            new ColorItem("Congo pink", 248, 131, 121),
                                            new ColorItem("Cool grey", 140, 146, 172),
                                            new ColorItem("Copper", 184, 115, 51),
                                            new ColorItem("Copper (Crayola)", 218, 138, 103),
                                            new ColorItem("Copper penny", 173, 111, 105),
                                            new ColorItem("Copper red", 203, 109, 81),
                                            new ColorItem("Copper rose", 153, 102, 102),
                                            new ColorItem("Coquelicot", 255, 56, 0),
                                            new ColorItem("Coral red", 255, 64, 64),
                                            new ColorItem("Cordovan", 137, 63, 69),
                                            new ColorItem("Corn", 251, 236, 93),
                                            new ColorItem("Cosmic latte", 255, 248, 231),
                                            new ColorItem("Coyote brown", 129, 97, 62),
                                            new ColorItem("Cotton candy", 255, 188, 217),
                                            new ColorItem("Cream", 255, 253, 208),
                                            new ColorItem("Crimson glory", 190, 0, 50),
                                            new ColorItem("Crimson red", 153, 0, 0),
                                            new ColorItem("Cyan azure", 78, 130, 180),
                                            new ColorItem("Cyan-blue azure", 70, 130, 191),
                                            new ColorItem("Cyan cobalt blue", 40, 88, 156),
                                            new ColorItem("Cyan cornflower blue", 24, 139, 194),
                                            new ColorItem("Cyan (process)", 0, 183, 235),
                                            new ColorItem("Cyber grape", 88, 66, 124),
                                            new ColorItem("Cyber yellow", 255, 211, 0),
                                            new ColorItem("Daffodil", 255, 255, 49),
                                            new ColorItem("Dandelion", 240, 225, 48),
                                            new ColorItem("Dark blue-gray", 102, 102, 153),
                                            new ColorItem("Dark brown", 101, 67, 33),
                                            new ColorItem("Dark brown-tangelo", 136, 101, 78),
                                            new ColorItem("Dark byzantium", 93, 57, 84),
                                            new ColorItem("Dark candy apple red", 164, 0, 0),
                                            new ColorItem("Dark cerulean", 8, 69, 126),
                                            new ColorItem("Dark chestnut", 152, 105, 96),
                                            new ColorItem("Dark coral", 205, 91, 69),
                                            new ColorItem("Dark electric blue", 83, 104, 120),
                                            new ColorItem("Dark green", 1, 50, 32),
                                            new ColorItem("Dark imperial blue", 0, 65, 106),
                                            new ColorItem("Dark jungle green", 26, 36, 33),
                                            new ColorItem("Dark lava", 72, 60, 50),
                                            new ColorItem("Dark lavender", 115, 79, 150),
                                            new ColorItem("Dark liver", 83, 75, 79),
                                            new ColorItem("Dark liver (horses)", 84, 61, 55),
                                            new ColorItem("Dark midnight blue", 0, 51, 102),
                                            new ColorItem("Dark moss green", 74, 93, 35),
                                            new ColorItem("Dark pastel blue", 119, 158, 203),
                                            new ColorItem("Dark pastel green", 3, 192, 60),
                                            new ColorItem("Dark pastel purple", 150, 111, 214),
                                            new ColorItem("Dark pastel red", 194, 59, 34),
                                            new ColorItem("Dark pink", 231, 84, 128),
                                            new ColorItem("Dark powder blue", 0, 51, 153),
                                            new ColorItem("Dark puce", 79, 58, 60),
                                            new ColorItem("Dark raspberry", 135, 38, 87),
                                            new ColorItem("Dark scarlet", 86, 3, 25),
                                            new ColorItem("Dark sienna", 60, 20, 20),
                                            new ColorItem("Dark sky blue", 140, 190, 214),
                                            new ColorItem("Dark spring green", 23, 114, 69),
                                            new ColorItem("Dark tan", 145, 129, 81),
                                            new ColorItem("Dark tangerine", 255, 168, 18),
                                            new ColorItem("Dark terra cotta", 204, 78, 92),
                                            new ColorItem("Dark vanilla", 209, 190, 168),
                                            new ColorItem("Dark yellow", 155, 135, 12),
                                            new ColorItem("Dartmouth green", 0, 112, 60),
                                            new ColorItem("Davy's grey", 85, 85, 85),
                                            new ColorItem("Debian red", 215, 10, 83),
                                            new ColorItem("Deep aquamarine", 64, 130, 109),
                                            new ColorItem("Deep carmine", 169, 32, 62),
                                            new ColorItem("Deep carmine pink", 239, 48, 56),
                                            new ColorItem("Deep carrot orange", 233, 105, 44),
                                            new ColorItem("Deep cerise", 218, 50, 135),
                                            new ColorItem("Deep champagne", 250, 214, 165),
                                            new ColorItem("Deep chestnut", 185, 78, 72),
                                            new ColorItem("Deep coffee", 112, 66, 65),
                                            new ColorItem("Deep fuchsia", 193, 84, 193),
                                            new ColorItem("Deep Green", 5, 102, 8),
                                            new ColorItem("Deep green-cyan turquoise", 14, 124, 97),
                                            new ColorItem("Deep jungle green", 0, 75, 73),
                                            new ColorItem("Deep koamaru", 51, 51, 102),
                                            new ColorItem("Deep lemon", 245, 199, 26),
                                            new ColorItem("Deep lilac", 153, 85, 187),
                                            new ColorItem("Deep magenta", 204, 0, 204),
                                            new ColorItem("Deep maroon", 130, 0, 0),
                                            new ColorItem("Deep mauve", 212, 115, 212),
                                            new ColorItem("Deep moss green", 53, 94, 59),
                                            new ColorItem("Deep peach", 255, 203, 164),
                                            new ColorItem("Deep puce", 169, 92, 104),
                                            new ColorItem("Deep Red", 133, 1, 1),
                                            new ColorItem("Deep ruby", 132, 63, 91),
                                            new ColorItem("Deep saffron", 255, 153, 51),
                                            new ColorItem("Deep Space Sparkle", 74, 100, 108),
                                            new ColorItem("Deep Taupe", 126, 94, 96),
                                            new ColorItem("Deep Tuscan red", 102, 66, 77),
                                            new ColorItem("Deep violet", 51, 0, 102),
                                            new ColorItem("Deer", 186, 135, 89),
                                            new ColorItem("Denim", 21, 96, 189),
                                            new ColorItem("Desaturated cyan", 102, 153, 153),
                                            new ColorItem("Desert sand", 237, 201, 175),
                                            new ColorItem("Desire", 234, 60, 83),
                                            new ColorItem("Diamond", 185, 242, 255),
                                            new ColorItem("Dirt", 155, 118, 83),
                                            new ColorItem("Dogwood rose", 215, 24, 104),
                                            new ColorItem("Dollar bill", 133, 187, 101),
                                            new ColorItem("Donkey brown", 102, 76, 40),
                                            new ColorItem("Duke blue", 0, 0, 156),
                                            new ColorItem("Dust storm", 229, 204, 201),
                                            new ColorItem("Dutch white", 239, 223, 187),
                                            new ColorItem("Earth yellow", 225, 169, 95),
                                            new ColorItem("Ebony", 85, 93, 80),
                                            new ColorItem("Ecru", 194, 178, 128),
                                            new ColorItem("Eerie black", 27, 27, 27),
                                            new ColorItem("Eggplant", 97, 64, 81),
                                            new ColorItem("Eggshell", 240, 234, 214),
                                            new ColorItem("Egyptian blue", 16, 52, 166),
                                            new ColorItem("Electric blue", 125, 249, 255),
                                            new ColorItem("Electric crimson", 255, 0, 63),
                                            new ColorItem("Electric indigo", 111, 0, 255),
                                            new ColorItem("Electric lime", 204, 255, 0),
                                            new ColorItem("Electric purple", 191, 0, 255),
                                            new ColorItem("Electric ultramarine", 63, 0, 255),
                                            new ColorItem("Electric violet", 143, 0, 255),
                                            new ColorItem("Electric yellow", 255, 255, 51),
                                            new ColorItem("Emerald", 80, 200, 120),
                                            new ColorItem("Eminence", 108, 48, 130),
                                            new ColorItem("English lavender", 180, 131, 149),
                                            new ColorItem("English red", 171, 75, 82),
                                            new ColorItem("English violet", 86, 60, 92),
                                            new ColorItem("Eton blue", 150, 200, 162),
                                            new ColorItem("Eucalyptus", 68, 215, 168),
                                            new ColorItem("Falu red", 128, 24, 24),
                                            new ColorItem("Fandango", 181, 51, 137),
                                            new ColorItem("Fandango pink", 222, 82, 133),
                                            new ColorItem("Fashion fuchsia", 244, 0, 161),
                                            new ColorItem("Fawn", 229, 170, 112),
                                            new ColorItem("Feldgrau", 77, 93, 83),
                                            new ColorItem("Feldspar", 253, 213, 177),
                                            new ColorItem("Fern green", 79, 121, 66),
                                            new ColorItem("Ferrari Red", 255, 40, 0),
                                            new ColorItem("Field drab", 108, 84, 30),
                                            new ColorItem("Fire engine red", 206, 32, 41),
                                            new ColorItem("Flame", 226, 88, 34),
                                            new ColorItem("Flamingo pink", 252, 142, 172),
                                            new ColorItem("Flavescent", 247, 233, 142),
                                            new ColorItem("Flax", 238, 220, 130),
                                            new ColorItem("Flirt", 162, 0, 109),
                                            new ColorItem("Folly", 255, 0, 79),
                                            new ColorItem("Forest green (traditional)", 1, 68, 33),
                                            new ColorItem("French bistre", 133, 109, 77),
                                            new ColorItem("French blue", 0, 114, 187),
                                            new ColorItem("French fuchsia", 253, 63, 146),
                                            new ColorItem("French lilac", 134, 96, 142),
                                            new ColorItem("French lime", 158, 253, 56),
                                            new ColorItem("French pink", 253, 108, 158),
                                            new ColorItem("French plum", 129, 20, 83),
                                            new ColorItem("French puce", 78, 22, 9),
                                            new ColorItem("French raspberry", 199, 44, 72),
                                            new ColorItem("French rose", 246, 74, 138),
                                            new ColorItem("French sky blue", 119, 181, 254),
                                            new ColorItem("French violet", 136, 6, 206),
                                            new ColorItem("French wine", 172, 30, 68),
                                            new ColorItem("Fresh Air", 166, 231, 255),
                                            new ColorItem("Fuchsia pink", 255, 119, 255),
                                            new ColorItem("Fuchsia purple", 204, 57, 123),
                                            new ColorItem("Fuchsia rose", 199, 67, 117),
                                            new ColorItem("Fulvous", 228, 132, 0),
                                            new ColorItem("Fuzzy Wuzzy", 204, 102, 102),
                                            new ColorItem("Gamboge", 228, 155, 15),
                                            new ColorItem("Gamboge orange (brown)", 153, 102, 0),
                                            new ColorItem("Generic viridian", 0, 127, 102),
                                            new ColorItem("Giants orange", 254, 90, 29),
                                            new ColorItem("Grussrel", 176, 101, 0),
                                            new ColorItem("Glaucous", 96, 130, 182),
                                            new ColorItem("Glitter", 230, 232, 250),
                                            new ColorItem("GO green", 0, 171, 102),
                                            new ColorItem("Gold (metallic)", 212, 175, 55),
                                            new ColorItem("Gold Fusion", 133, 117, 78),
                                            new ColorItem("Golden brown", 153, 101, 21),
                                            new ColorItem("Golden poppy", 252, 194, 0),
                                            new ColorItem("Golden yellow", 255, 223, 0),
                                            new ColorItem("Granny Smith Apple", 168, 228, 160),
                                            new ColorItem("Grape", 111, 45, 168),
                                            new ColorItem("Gray (X11 gray)", 190, 190, 190),
                                            new ColorItem("Gray-asparagus", 70, 89, 69),
                                            new ColorItem("Green (Crayola)", 28, 172, 120),
                                            new ColorItem("Green (Munsell)", 0, 168, 119),
                                            new ColorItem("Green (NCS)", 0, 159, 107),
                                            new ColorItem("Green (Pantone)", 0, 173, 67),
                                            new ColorItem("Green (pigment)", 0, 165, 80),
                                            new ColorItem("Green (RYB)", 102, 176, 50),
                                            new ColorItem("Green-blue", 17, 100, 180),
                                            new ColorItem("Green-cyan", 0, 153, 102),
                                            new ColorItem("Grizzly", 136, 88, 24),
                                            new ColorItem("Grullo", 169, 154, 134),
                                            new ColorItem("Halayà úbe", 102, 56, 84),
                                            new ColorItem("Han blue", 68, 108, 207),
                                            new ColorItem("Han purple", 82, 24, 250),
                                            new ColorItem("Harlequin", 63, 255, 0),
                                            new ColorItem("Harlequin green", 70, 203, 24),
                                            new ColorItem("Harvard crimson", 201, 0, 22),
                                            new ColorItem("Harvest gold", 218, 145, 0),
                                            new ColorItem("Heliotrope", 223, 115, 255),
                                            new ColorItem("Heliotrope gray", 170, 152, 169),
                                            new ColorItem("Heliotrope magenta", 170, 0, 187),
                                            new ColorItem("Honolulu blue", 0, 109, 176),
                                            new ColorItem("Hooker's green", 73, 121, 107),
                                            new ColorItem("Hot magenta", 255, 29, 206),
                                            new ColorItem("Iceberg", 113, 166, 210),
                                            new ColorItem("Icterine", 252, 247, 94),
                                            new ColorItem("Illuminating Emerald", 49, 145, 119),
                                            new ColorItem("Imperial", 96, 47, 107),
                                            new ColorItem("Imperial blue", 0, 35, 149),
                                            new ColorItem("Imperial purple", 102, 2, 60),
                                            new ColorItem("Imperial red", 237, 41, 57),
                                            new ColorItem("Inchworm", 178, 236, 93),
                                            new ColorItem("Independence", 76, 81, 109),
                                            new ColorItem("India green", 19, 136, 8),
                                            new ColorItem("Indian yellow", 227, 168, 87),
                                            new ColorItem("Indigo dye", 9, 31, 146),
                                            new ColorItem("International Klein Blue", 0, 47, 167),
                                            new ColorItem("International orange (aerospace)", 255, 79, 0),
                                            new ColorItem("International orange (engineering)", 186, 22, 12),
                                            new ColorItem("International orange (Golden Gate Bridge)", 192, 54, 44),
                                            new ColorItem("Iris", 90, 79, 207),
                                            new ColorItem("Irresistible", 179, 68, 108),
                                            new ColorItem("Isabelline", 244, 240, 236),
                                            new ColorItem("Islamic green", 0, 144, 0),
                                            new ColorItem("Jade", 0, 168, 107),
                                            new ColorItem("Japanese carmine", 157, 41, 51),
                                            new ColorItem("Japanese indigo", 38, 67, 72),
                                            new ColorItem("Japanese violet", 91, 50, 86),
                                            new ColorItem("Jasmine", 248, 222, 126),
                                            new ColorItem("Jasper", 215, 59, 62),
                                            new ColorItem("Jazzberry jam", 165, 11, 94),
                                            new ColorItem("Jelly Bean", 218, 97, 78),
                                            new ColorItem("Jet", 52, 52, 52),
                                            new ColorItem("Jonquil", 244, 202, 22),
                                            new ColorItem("Jordy blue", 138, 185, 241),
                                            new ColorItem("June bud", 189, 218, 87),
                                            new ColorItem("Jungle green", 41, 171, 135),
                                            new ColorItem("Kelly green", 76, 187, 23),
                                            new ColorItem("Kenyan copper", 124, 28, 5),
                                            new ColorItem("Keppel", 58, 176, 158),
                                            new ColorItem("Jawad/Chicken Color (HTML/CSS) (Khaki)", 195, 176, 145),
                                            new ColorItem("Kobe", 136, 45, 23),
                                            new ColorItem("Kobi", 231, 159, 196),
                                            new ColorItem("Kombu green", 53, 66, 48),
                                            new ColorItem("KU Crimson", 232, 0, 13),
                                            new ColorItem("La Salle Green", 8, 120, 48),
                                            new ColorItem("Languid lavender", 214, 202, 221),
                                            new ColorItem("Lapis lazuli", 38, 97, 156),
                                            new ColorItem("Laser Lemon", 255, 255, 102),
                                            new ColorItem("Laurel green", 169, 186, 157),
                                            new ColorItem("Lava", 207, 16, 32),
                                            new ColorItem("Lavender (floral)", 181, 126, 220),
                                            new ColorItem("Lavender blue", 204, 204, 255),
                                            new ColorItem("Lavender gray", 196, 195, 208),
                                            new ColorItem("Lavender indigo", 148, 87, 235),
                                            new ColorItem("Lavender pink", 251, 174, 210),
                                            new ColorItem("Lavender purple", 150, 123, 182),
                                            new ColorItem("Lavender rose", 251, 160, 227),
                                            new ColorItem("Lemon", 255, 247, 0),
                                            new ColorItem("Lemon curry", 204, 160, 29),
                                            new ColorItem("Lemon glacier", 253, 255, 0),
                                            new ColorItem("Lemon lime", 227, 255, 0),
                                            new ColorItem("Lemon meringue", 246, 234, 190),
                                            new ColorItem("Lemon yellow", 255, 244, 79),
                                            new ColorItem("Lenurple", 186, 147, 216),
                                            new ColorItem("Licorice", 26, 17, 16),
                                            new ColorItem("Liberty", 84, 90, 167),
                                            new ColorItem("Light brilliant red", 254, 46, 46),
                                            new ColorItem("Light brown", 181, 101, 29),
                                            new ColorItem("Light carmine pink", 230, 103, 113),
                                            new ColorItem("Light cobalt blue", 136, 172, 224),
                                            new ColorItem("Light cornflower blue", 147, 204, 234),
                                            new ColorItem("Light crimson", 245, 105, 145),
                                            new ColorItem("Light deep pink", 255, 92, 205),
                                            new ColorItem("Light French beige", 200, 173, 127),
                                            new ColorItem("Light fuchsia pink", 249, 132, 239),
                                            new ColorItem("Light grayish magenta", 204, 153, 204),
                                            new ColorItem("Light hot pink", 255, 179, 222),
                                            new ColorItem("Light medium orchid", 211, 155, 203),
                                            new ColorItem("Light moss green", 173, 223, 173),
                                            new ColorItem("Light orchid", 230, 168, 215),
                                            new ColorItem("Light pastel purple", 177, 156, 217),
                                            new ColorItem("Light salmon pink", 255, 153, 153),
                                            new ColorItem("Light taupe", 179, 139, 109),
                                            new ColorItem("Lilac", 200, 162, 200),
                                            new ColorItem("Limerick", 157, 194, 9),
                                            new ColorItem("Lincoln green", 25, 89, 5),
                                            new ColorItem("Little boy blue", 108, 160, 220),
                                            new ColorItem("Liver", 103, 76, 71),
                                            new ColorItem("Liver (dogs)", 184, 109, 41),
                                            new ColorItem("Liver (organ)", 108, 46, 31),
                                            new ColorItem("Liver chestnut", 152, 116, 86),
                                            new ColorItem("Lumber", 255, 228, 205),
                                            new ColorItem("Lust", 230, 32, 32),
                                            new ColorItem("Magenta (dye)", 202, 31, 123),
                                            new ColorItem("Magenta (Pantone)", 208, 65, 126),
                                            new ColorItem("Magenta (process)", 255, 0, 144),
                                            new ColorItem("Magenta haze", 159, 69, 118),
                                            new ColorItem("Magenta-pink", 204, 51, 139),
                                            new ColorItem("Magic mint", 170, 240, 209),
                                            new ColorItem("Magnolia", 248, 244, 255),
                                            new ColorItem("Mahogany", 192, 64, 0),
                                            new ColorItem("Majorelle Blue", 96, 80, 220),
                                            new ColorItem("Malachite", 11, 218, 81),
                                            new ColorItem("Manatee", 151, 154, 170),
                                            new ColorItem("Mango Tango", 255, 130, 67),
                                            new ColorItem("Mantis", 116, 195, 101),
                                            new ColorItem("Mardi Gras", 136, 0, 133),
                                            new ColorItem("Maroon (X11)", 176, 48, 96),
                                            new ColorItem("Mauve", 224, 176, 255),
                                            new ColorItem("Mauve taupe", 145, 95, 109),
                                            new ColorItem("Mauvelous", 239, 152, 170),
                                            new ColorItem("May green", 76, 145, 65),
                                            new ColorItem("Maya blue", 115, 194, 251),
                                            new ColorItem("Meat brown", 229, 183, 59),
                                            new ColorItem("Medium aquamarine", 102, 221, 170),
                                            new ColorItem("Medium candy apple red", 226, 6, 44),
                                            new ColorItem("Medium carmine", 175, 64, 53),
                                            new ColorItem("Medium champagne", 243, 229, 171),
                                            new ColorItem("Medium electric blue", 3, 80, 150),
                                            new ColorItem("Medium jungle green", 28, 53, 45),
                                            new ColorItem("Medium lavender magenta", 221, 160, 221),
                                            new ColorItem("Medium Persian blue", 0, 103, 165),
                                            new ColorItem("Medium red-violet", 187, 51, 133),
                                            new ColorItem("Medium ruby", 170, 64, 105),
                                            new ColorItem("Medium sky blue", 128, 218, 235),
                                            new ColorItem("Medium spring bud", 201, 220, 135),
                                            new ColorItem("Medium vermilion", 217, 96, 59),
                                            new ColorItem("Mellow apricot", 248, 184, 120),
                                            new ColorItem("Melon", 253, 188, 180),
                                            new ColorItem("Metallic Seaweed", 10, 126, 140),
                                            new ColorItem("Metallic Sunburst", 156, 124, 56),
                                            new ColorItem("Mexican pink", 228, 0, 124),
                                            new ColorItem("Midnight green (eagle green)", 0, 73, 83),
                                            new ColorItem("Mikado yellow", 255, 196, 12),
                                            new ColorItem("Mindaro", 227, 249, 136),
                                            new ColorItem("Mint", 62, 180, 137),
                                            new ColorItem("Mint green", 152, 255, 152),
                                            new ColorItem("Moonstone blue", 115, 169, 194),
                                            new ColorItem("Mordant red 19", 174, 12, 0),
                                            new ColorItem("Moss green", 138, 154, 91),
                                            new ColorItem("Mountain Meadow", 48, 186, 143),
                                            new ColorItem("Mountbatten pink", 153, 122, 141),
                                            new ColorItem("MSU Green", 24, 69, 59),
                                            new ColorItem("Mughal green", 48, 96, 48),
                                            new ColorItem("Mulberry", 197, 75, 140),
                                            new ColorItem("Mustard", 255, 219, 88),
                                            new ColorItem("Myrtle green", 49, 120, 115),
                                            new ColorItem("", 246, 173, 198),
                                            new ColorItem("Napier green", 42, 128, 0),
                                            new ColorItem("Naples yellow", 250, 218, 94),
                                            new ColorItem("Neon Carrot", 255, 163, 67),
                                            new ColorItem("Neon fuchsia", 254, 65, 100),
                                            new ColorItem("Neon green", 57, 255, 20),
                                            new ColorItem("New Car", 33, 79, 198),
                                            new ColorItem("New York pink", 215, 131, 127),
                                            new ColorItem("Non-photo blue", 164, 221, 237),
                                            new ColorItem("North Texas Green", 5, 144, 51),
                                            new ColorItem("Nyanza", 233, 255, 219),
                                            new ColorItem("Ocean Boat Blue", 0, 119, 190),
                                            new ColorItem("Ochre", 204, 119, 34),
                                            new ColorItem("Old burgundy", 67, 48, 46),
                                            new ColorItem("Old gold", 207, 181, 59),
                                            new ColorItem("Old lavender", 121, 104, 120),
                                            new ColorItem("Old mauve", 103, 49, 71),
                                            new ColorItem("Old moss green", 134, 126, 54),
                                            new ColorItem("Old rose", 192, 128, 129),
                                            new ColorItem("Olive Drab #7", 60, 52, 31),
                                            new ColorItem("Olivine", 154, 185, 115),
                                            new ColorItem("Onyx", 53, 56, 57),
                                            new ColorItem("Opera mauve", 183, 132, 167),
                                            new ColorItem("Orange (color wheel)", 255, 127, 0),
                                            new ColorItem("Orange (Crayola)", 255, 117, 56),
                                            new ColorItem("Orange (Pantone)", 255, 88, 0),
                                            new ColorItem("Orange (RYB)", 251, 153, 2),
                                            new ColorItem("Orange peel", 255, 159, 0),
                                            new ColorItem("Orchid pink", 242, 189, 205),
                                            new ColorItem("Orioles orange", 251, 79, 20),
                                            new ColorItem("Outer Space", 65, 74, 76),
                                            new ColorItem("Outrageous Orange", 255, 110, 74),
                                            new ColorItem("Oxford Blue", 0, 33, 71),
                                            new ColorItem("Pakistan green", 0, 102, 0),
                                            new ColorItem("Palatinate blue", 39, 59, 226),
                                            new ColorItem("Palatinate purple", 104, 40, 96),
                                            new ColorItem("Pale brown", 152, 118, 84),
                                            new ColorItem("Pale cerulean", 155, 196, 226),
                                            new ColorItem("Pale chestnut", 221, 173, 175),
                                            new ColorItem("Pale cornflower blue", 171, 205, 239),
                                            new ColorItem("Pale cyan", 135, 211, 248),
                                            new ColorItem("Pale gold", 230, 190, 138),
                                            new ColorItem("Pale lavender", 220, 208, 255),
                                            new ColorItem("Pale magenta", 249, 132, 229),
                                            new ColorItem("Pale magenta-pink", 255, 153, 204),
                                            new ColorItem("Pale pink", 250, 218, 221),
                                            new ColorItem("Pale robin egg blue", 150, 222, 209),
                                            new ColorItem("Pale silver", 201, 192, 187),
                                            new ColorItem("Pale spring bud", 236, 235, 189),
                                            new ColorItem("Pale taupe", 188, 152, 126),
                                            new ColorItem("Pale violet", 204, 153, 255),
                                            new ColorItem("Pansy purple", 120, 24, 74),
                                            new ColorItem("Paolo Veronese green", 0, 155, 125),
                                            new ColorItem("Paradise pink", 230, 62, 98),
                                            new ColorItem("Pastel blue", 174, 198, 207),
                                            new ColorItem("Pastel brown", 131, 105, 83),
                                            new ColorItem("Pastel gray", 207, 207, 196),
                                            new ColorItem("Pastel green", 119, 221, 119),
                                            new ColorItem("Pastel magenta", 244, 154, 194),
                                            new ColorItem("Pastel orange", 255, 179, 71),
                                            new ColorItem("Pastel pink", 222, 165, 164),
                                            new ColorItem("Pastel purple", 179, 158, 181),
                                            new ColorItem("Pastel red", 255, 105, 97),
                                            new ColorItem("Pastel violet", 203, 153, 201),
                                            new ColorItem("Pastel yellow", 253, 253, 150),
                                            new ColorItem("Peach", 255, 229, 180),
                                            new ColorItem("Peach-orange", 255, 204, 153),
                                            new ColorItem("Peach-yellow", 250, 223, 173),
                                            new ColorItem("Pear", 209, 226, 49),
                                            new ColorItem("Pearl", 234, 224, 200),
                                            new ColorItem("Pearl Aqua", 136, 216, 192),
                                            new ColorItem("Pearly purple", 183, 104, 162),
                                            new ColorItem("Peridot", 230, 226, 0),
                                            new ColorItem("Persian blue", 28, 57, 187),
                                            new ColorItem("Persian green", 0, 166, 147),
                                            new ColorItem("Persian indigo", 50, 18, 122),
                                            new ColorItem("Persian orange", 217, 144, 88),
                                            new ColorItem("Persian pink", 247, 127, 190),
                                            new ColorItem("Persian plum", 112, 28, 28),
                                            new ColorItem("Persian red", 204, 51, 51),
                                            new ColorItem("Persian rose", 254, 40, 162),
                                            new ColorItem("Persimmon", 236, 88, 0),
                                            new ColorItem("Phlox", 223, 0, 255),
                                            new ColorItem("Phthalo blue", 0, 15, 137),
                                            new ColorItem("Phthalo green", 18, 53, 36),
                                            new ColorItem("Picton blue", 69, 177, 232),
                                            new ColorItem("Pictorial carmine", 195, 11, 78),
                                            new ColorItem("Piggy pink", 253, 221, 230),
                                            new ColorItem("Pine green", 1, 121, 111),
                                            new ColorItem("Pink (Pantone)", 215, 72, 148),
                                            new ColorItem("Pink lace", 255, 221, 244),
                                            new ColorItem("Pink lavender", 216, 178, 209),
                                            new ColorItem("Pink pearl", 231, 172, 207),
                                            new ColorItem("Pink raspberry", 152, 0, 54),
                                            new ColorItem("Pink Sherbet", 247, 143, 167),
                                            new ColorItem("Pistachio", 147, 197, 114),
                                            new ColorItem("Platinum", 229, 228, 226),
                                            new ColorItem("Popstar", 190, 79, 98),
                                            new ColorItem("Portland Orange", 255, 90, 54),
                                            new ColorItem("Princeton orange", 245, 128, 37),
                                            new ColorItem("Prussian blue", 0, 49, 83),
                                            new ColorItem("Puce", 204, 136, 153),
                                            new ColorItem("Puce red", 114, 47, 55),
                                            new ColorItem("Pullman Brown (UPS Brown)", 100, 65, 23),
                                            new ColorItem("Pullman Green", 59, 51, 28),
                                            new ColorItem("Pumpkin", 255, 117, 24),
                                            new ColorItem("Purple (Munsell)", 159, 0, 197),
                                            new ColorItem("Purple (X11)", 160, 32, 240),
                                            new ColorItem("Purple Heart", 105, 53, 156),
                                            new ColorItem("Purple mountain majesty", 150, 120, 182),
                                            new ColorItem("Purple navy", 78, 81, 128),
                                            new ColorItem("Purple pizzazz", 254, 78, 218),
                                            new ColorItem("Purple taupe", 80, 64, 77),
                                            new ColorItem("Purpureus", 154, 78, 174),
                                            new ColorItem("Quartz", 81, 72, 79),
                                            new ColorItem("Queen blue", 67, 107, 149),
                                            new ColorItem("Queen pink", 232, 204, 215),
                                            new ColorItem("Quinacridone magenta", 142, 58, 89),
                                            new ColorItem("Radical Red", 255, 53, 94),
                                            new ColorItem("Rajah", 251, 171, 96),
                                            new ColorItem("Raspberry", 227, 11, 93),
                                            new ColorItem("Raspberry pink", 226, 80, 152),
                                            new ColorItem("Raw umber", 130, 102, 68),
                                            new ColorItem("Razzle dazzle rose", 255, 51, 204),
                                            new ColorItem("Razzmatazz", 227, 37, 107),
                                            new ColorItem("Razzmic Berry", 141, 78, 133),
                                            new ColorItem("Rebecca Purple", 102, 51, 153),
                                            new ColorItem("Red (Crayola)", 238, 32, 77),
                                            new ColorItem("Red (Munsell)", 242, 0, 60),
                                            new ColorItem("Red (NCS)", 196, 2, 51),
                                            new ColorItem("Red (pigment)", 237, 28, 36),
                                            new ColorItem("Red (RYB)", 254, 39, 18),
                                            new ColorItem("Red devil", 134, 1, 17),
                                            new ColorItem("Red-orange", 255, 83, 73),
                                            new ColorItem("Red-purple", 228, 0, 120),
                                            new ColorItem("Redwood", 164, 90, 82),
                                            new ColorItem("Regalia", 82, 45, 128),
                                            new ColorItem("Resolution blue", 0, 35, 135),
                                            new ColorItem("Rhythm", 119, 118, 150),
                                            new ColorItem("Rich black", 0, 64, 64),
                                            new ColorItem("Rich brilliant lavender", 241, 167, 254),
                                            new ColorItem("Rich electric blue", 8, 146, 208),
                                            new ColorItem("Rich lavender", 167, 107, 207),
                                            new ColorItem("Rich lilac", 182, 102, 210),
                                            new ColorItem("Rifle green", 68, 76, 56),
                                            new ColorItem("Robin egg blue", 0, 204, 204),
                                            new ColorItem("Rocket metallic", 138, 127, 128),
                                            new ColorItem("Roman silver", 131, 137, 150),
                                            new ColorItem("Rose bonbon", 249, 66, 158),
                                            new ColorItem("Rose ebony", 103, 72, 70),
                                            new ColorItem("Rose gold", 183, 110, 121),
                                            new ColorItem("Rose pink", 255, 102, 204),
                                            new ColorItem("Rose red", 194, 30, 86),
                                            new ColorItem("Rose taupe", 144, 93, 93),
                                            new ColorItem("Rose vale", 171, 78, 82),
                                            new ColorItem("Rosewood", 101, 0, 11),
                                            new ColorItem("Rosso corsa", 212, 0, 0),
                                            new ColorItem("Royal azure", 0, 56, 168),
                                            new ColorItem("Royal blue", 0, 35, 102),
                                            new ColorItem("Royal fuchsia", 202, 44, 146),
                                            new ColorItem("Royal purple", 120, 81, 169),
                                            new ColorItem("Ruber", 206, 70, 118),
                                            new ColorItem("Rubine red", 209, 0, 86),
                                            new ColorItem("Ruby", 224, 17, 95),
                                            new ColorItem("Ruby red", 155, 17, 30),
                                            new ColorItem("Ruddy", 255, 0, 40),
                                            new ColorItem("Ruddy brown", 187, 101, 40),
                                            new ColorItem("Ruddy pink", 225, 142, 150),
                                            new ColorItem("Rufous", 168, 28, 7),
                                            new ColorItem("Russet", 128, 70, 27),
                                            new ColorItem("Russian green", 103, 146, 103),
                                            new ColorItem("Russian violet", 50, 23, 77),
                                            new ColorItem("Rust", 183, 65, 14),
                                            new ColorItem("Rusty red", 218, 44, 67),
                                            new ColorItem("Safety orange", 255, 120, 0),
                                            new ColorItem("Safety orange (blaze orange)", 255, 103, 0),
                                            new ColorItem("Safety yellow", 238, 210, 2),
                                            new ColorItem("Saffron", 244, 196, 48),
                                            new ColorItem("Sage", 188, 184, 138),
                                            new ColorItem("St. Patrick's blue", 35, 41, 122),
                                            new ColorItem("Salmon pink", 255, 145, 164),
                                            new ColorItem("Sandstorm", 236, 213, 64),
                                            new ColorItem("Sangria", 146, 0, 10),
                                            new ColorItem("Sap green", 80, 125, 42),
                                            new ColorItem("Sapphire", 15, 82, 186),
                                            new ColorItem("Satin sheen gold", 203, 161, 53),
                                            new ColorItem("Scarlet", 255, 36, 0),
                                            new ColorItem("Tractor red", 253, 14, 53),
                                            new ColorItem("School bus yellow", 255, 216, 0),
                                            new ColorItem("Screamin' Green", 118, 255, 122),
                                            new ColorItem("Sea blue", 0, 105, 148),
                                            new ColorItem("Seal brown", 50, 20, 20),
                                            new ColorItem("Selective yellow", 255, 186, 0),
                                            new ColorItem("Sepia", 112, 66, 20),
                                            new ColorItem("Shadow", 138, 121, 93),
                                            new ColorItem("Shadow blue", 119, 139, 165),
                                            new ColorItem("Shampoo", 255, 207, 241),
                                            new ColorItem("Shamrock green", 0, 158, 96),
                                            new ColorItem("Sheen Green", 143, 212, 0),
                                            new ColorItem("Shimmering Blush", 217, 134, 149),
                                            new ColorItem("Shocking pink", 252, 15, 192),
                                            new ColorItem("Shocking pink (Crayola)", 255, 111, 255),
                                            new ColorItem("Silver chalice", 172, 172, 172),
                                            new ColorItem("Silver Lake blue", 93, 137, 186),
                                            new ColorItem("Silver pink", 196, 174, 173),
                                            new ColorItem("Silver sand", 191, 193, 194),
                                            new ColorItem("Sinopia", 203, 65, 11),
                                            new ColorItem("Skobeloff", 0, 116, 116),
                                            new ColorItem("Sky magenta", 207, 113, 175),
                                            new ColorItem("Smitten", 200, 65, 134),
                                            new ColorItem("Smoke", 115, 130, 118),
                                            new ColorItem("Smoky black", 16, 12, 8),
                                            new ColorItem("Smoky Topaz", 147, 61, 65),
                                            new ColorItem("Soap", 206, 200, 239),
                                            new ColorItem("Solid pink", 137, 56, 67),
                                            new ColorItem("Sonic silver", 117, 117, 117),
                                            new ColorItem("Spartan Crimson", 158, 19, 22),
                                            new ColorItem("Space cadet", 29, 41, 81),
                                            new ColorItem("Spanish bistre", 128, 117, 50),
                                            new ColorItem("Spanish blue", 0, 112, 184),
                                            new ColorItem("Spanish carmine", 209, 0, 71),
                                            new ColorItem("Spanish crimson", 229, 26, 76),
                                            new ColorItem("Spanish gray", 152, 152, 152),
                                            new ColorItem("Spanish green", 0, 145, 80),
                                            new ColorItem("Spanish orange", 232, 97, 0),
                                            new ColorItem("Spanish pink", 247, 191, 190),
                                            new ColorItem("Spanish red", 230, 0, 38),
                                            new ColorItem("Spanish violet", 76, 40, 130),
                                            new ColorItem("Spanish viridian", 0, 127, 92),
                                            new ColorItem("Spicy mix", 139, 95, 77),
                                            new ColorItem("Spiro Disco Ball", 15, 192, 252),
                                            new ColorItem("Spring bud", 167, 252, 0),
                                            new ColorItem("Star command blue", 0, 123, 184),
                                            new ColorItem("Steel pink", 204, 51, 204),
                                            new ColorItem("Stormcloud", 79, 102, 106),
                                            new ColorItem("Straw", 228, 217, 111),
                                            new ColorItem("Strawberry", 252, 90, 141),
                                            new ColorItem("Sunglow", 255, 204, 51),
                                            new ColorItem("Sunray", 227, 171, 87),
                                            new ColorItem("Sunset orange", 253, 94, 83),
                                            new ColorItem("Super pink", 207, 107, 169),
                                            new ColorItem("Tangelo", 249, 77, 0),
                                            new ColorItem("Tangerine", 242, 133, 0),
                                            new ColorItem("Tangerine yellow", 255, 204, 0),
                                            new ColorItem("Taupe gray", 139, 133, 137),
                                            new ColorItem("Tea green", 208, 240, 192),
                                            new ColorItem("Teal blue", 54, 117, 136),
                                            new ColorItem("Teal deer", 153, 230, 179),
                                            new ColorItem("Teal green", 0, 130, 127),
                                            new ColorItem("Telemagenta", 207, 52, 118),
                                            new ColorItem("Tenné", 205, 87, 0),
                                            new ColorItem("Terra cotta", 226, 114, 91),
                                            new ColorItem("Tickle Me Pink", 252, 137, 172),
                                            new ColorItem("Tiffany Blue", 10, 186, 181),
                                            new ColorItem("Tiger's eye", 224, 141, 60),
                                            new ColorItem("Timberwolf", 219, 215, 210),
                                            new ColorItem("Titanium yellow", 238, 230, 0),
                                            new ColorItem("Toolbox", 116, 108, 192),
                                            new ColorItem("Topaz", 255, 200, 124),
                                            new ColorItem("Tropical rain forest", 0, 117, 94),
                                            new ColorItem("True Blue", 0, 115, 207),
                                            new ColorItem("Tufts Blue", 65, 125, 193),
                                            new ColorItem("Tulip", 255, 135, 141),
                                            new ColorItem("Tumbleweed", 222, 170, 136),
                                            new ColorItem("Turkish rose", 181, 114, 129),
                                            new ColorItem("Turquoise blue", 0, 255, 239),
                                            new ColorItem("Turquoise green", 160, 214, 180),
                                            new ColorItem("Tuscan red", 124, 72, 72),
                                            new ColorItem("Tuscany", 192, 153, 153),
                                            new ColorItem("Twilight lavender", 138, 73, 107),
                                            new ColorItem("UA blue", 0, 51, 170),
                                            new ColorItem("UA red", 217, 0, 76),
                                            new ColorItem("Ube", 136, 120, 195),
                                            new ColorItem("UCLA Blue", 83, 104, 149),
                                            new ColorItem("UCLA Gold", 255, 179, 0),
                                            new ColorItem("UFO Green", 60, 208, 112),
                                            new ColorItem("Ultramarine", 18, 10, 143),
                                            new ColorItem("Ultramarine blue", 65, 102, 245),
                                            new ColorItem("Ultra red", 252, 108, 133),
                                            new ColorItem("Umber", 99, 81, 71),
                                            new ColorItem("Unbleached silk", 255, 221, 202),
                                            new ColorItem("United Nations blue", 91, 146, 229),
                                            new ColorItem("University of California Gold", 183, 135, 39),
                                            new ColorItem("UP Maroon", 123, 17, 19),
                                            new ColorItem("Upsdell red", 174, 32, 41),
                                            new ColorItem("Urobilin", 225, 173, 33),
                                            new ColorItem("USAFA blue", 0, 79, 152),
                                            new ColorItem("University of Tennessee Orange", 247, 127, 0),
                                            new ColorItem("Utah Crimson", 211, 0, 63),
                                            new ColorItem("Vanilla ice", 243, 143, 169),
                                            new ColorItem("Vegas gold", 197, 179, 88),
                                            new ColorItem("Venetian red", 200, 8, 21),
                                            new ColorItem("Verdigris", 67, 179, 174),
                                            new ColorItem("Vermilion", 217, 56, 30),
                                            new ColorItem("Very light azure", 116, 187, 251),
                                            new ColorItem("Very light blue", 102, 102, 255),
                                            new ColorItem("Very light malachite green", 100, 233, 134),
                                            new ColorItem("Very light tangelo", 255, 176, 119),
                                            new ColorItem("Very pale orange", 255, 223, 191),
                                            new ColorItem("Very pale yellow", 255, 255, 191),
                                            new ColorItem("Violet (color wheel)", 127, 0, 255),
                                            new ColorItem("Violet (RYB)", 134, 1, 175),
                                            new ColorItem("Violet-blue", 50, 74, 178),
                                            new ColorItem("Violet-red", 247, 83, 148),
                                            new ColorItem("Viridian green", 0, 150, 152),
                                            new ColorItem("Vista blue", 124, 158, 217),
                                            new ColorItem("Vivid amber", 204, 153, 0),
                                            new ColorItem("Vivid auburn", 146, 39, 36),
                                            new ColorItem("Vivid burgundy", 159, 29, 53),
                                            new ColorItem("Vivid cerise", 218, 29, 129),
                                            new ColorItem("Vivid cerulean", 0, 170, 238),
                                            new ColorItem("Vivid crimson", 204, 0, 51),
                                            new ColorItem("Vivid gamboge", 255, 153, 0),
                                            new ColorItem("Vivid lime green", 166, 214, 8),
                                            new ColorItem("Vivid malachite", 0, 204, 51),
                                            new ColorItem("Vivid mulberry", 184, 12, 227),
                                            new ColorItem("Vivid orange", 255, 95, 0),
                                            new ColorItem("Vivid orange peel", 255, 160, 0),
                                            new ColorItem("Vivid orchid", 204, 0, 255),
                                            new ColorItem("Vivid raspberry", 255, 0, 108),
                                            new ColorItem("Vivid red", 247, 13, 26),
                                            new ColorItem("Vivid red-tangelo", 223, 97, 36),
                                            new ColorItem("Vivid sky blue", 0, 204, 255),
                                            new ColorItem("Vivid tangelo", 240, 116, 39),
                                            new ColorItem("Vivid tangerine", 255, 160, 137),
                                            new ColorItem("Vivid vermilion", 229, 96, 36),
                                            new ColorItem("Vivid violet", 159, 0, 255),
                                            new ColorItem("Vivid yellow", 255, 227, 2),
                                            new ColorItem("Warm black", 0, 66, 66),
                                            new ColorItem("Waterspout", 164, 244, 249),
                                            new ColorItem("Wenge", 100, 84, 82),
                                            new ColorItem("Wild blue yonder", 162, 173, 208),
                                            new ColorItem("Wild orchid", 212, 112, 162),
                                            new ColorItem("Wild Strawberry", 255, 67, 164),
                                            new ColorItem("Willpower orange", 253, 88, 0),
                                            new ColorItem("Windsor tan", 167, 85, 2),
                                            new ColorItem("Wisteria", 201, 160, 220),
                                            new ColorItem("Xanadu", 115, 134, 120),
                                            new ColorItem("Yale Blue", 15, 77, 146),
                                            new ColorItem("Yankees blue", 28, 40, 65),
                                            new ColorItem("Yellow (Crayola)", 252, 232, 131),
                                            new ColorItem("Yellow (Munsell)", 239, 204, 0),
                                            new ColorItem("Yellow (Pantone)", 254, 223, 0),
                                            new ColorItem("Yellow (RYB)", 254, 254, 51),
                                            new ColorItem("Yellow Orange", 255, 174, 66),
                                            new ColorItem("Yellow rose", 255, 240, 0),
                                            new ColorItem("Zaffre", 0, 20, 168),
                                            new ColorItem("Zinnwaldite brown", 44, 22, 8),
                                            new ColorItem("Zomp", 57, 167, 142)}; 
  /* 
   * The tree set below contains all of the colors in sorted order. 
   * They are in the same order as the items array. This allows the
   * colors to be obtained in order, from the first to the last. In
   * practice, what is really needed (and done) is to return the
   * next available color from the palette. Each entry in the set is an 
   * index value of the color in the items array. Note that when a color 
   * is pulled from this set, the set entry is removed. When the color is 
   * returned to the pool, the set entry is added back. This means that 
   * this tree set is the actual color palette pool.
   */
  final private TreeSet<Integer> itemsTree = new TreeSet<Integer>();
  /* 
   * The hash map below maps color names to color numbers. Color
   * numbers are not RGB values (in this case), but index values
   * for the original color items array. This map is required so 
   * that colors can be pulled from the pool by name, rather than 
   * just in next available color order. This map is never changed 
   * (at all) once it is created. The key to this hash map is the 
   * color name (a Java string). The value is the color number.
   */
  final private HashMap<String, Integer> itemsByName = 
    new HashMap<String, Integer>();
  /*
   * The hash map below maps color RGB values to color numbers. Color
   * numbers are not RGB values (in this case), but index values
   * for the original color items array. This map is required so 
   * that colors can be returned to the color pool, by RGB value. 
   * This is important because when a color is pulled from the 
   * color pool, all the caller gets is the RGB value. This means 
   * that the caller can only return the color back to the pool 
   * using the RGB value. This map is never changed (at all) once 
   * it is created. The key to this hash map is the RGB value. The 
   * value is the color number.
   */
  final private HashMap<Integer, Integer> itemsByRgb = 
      new HashMap<Integer, Integer>();
  /**
   * The constructor below is used to create instances of this class.
   * It can be invoked any number of times as need be. Note that the
   * constructor loads the color pool (the color tree) with all of 
   * the colors. Colors are then pulled from the pool and returned
   * to the pool, as need be. The two hash maps are never modified
   * after they are built in this constructor.
   */  
  public ColorPalette() {     
    /* Build the items tree map */
    for(int i = 0; i < items.length; i++)
      itemsTree.add(i);
    /* Build the color name to color index mapping */
    for(int i = 0; i < items.length; i++)
      itemsByName.put(items[i].name, i);
    /* Build the color RGB value to color index mapping */
    for(int i = 0; i < items.length; i++)
      itemsByRgb.put(items[i].rgb, i);   
  }
  /** 
   * Get the number of colors left in the color pool. The number
   * of remaining colors will be greater than or equal to zero.
   * If the color pool is empty, then this method will return
   * zero.
   *   
   * @return  the number of colors left in the color pool
   * @see     int
   */
  public int colorsLeft() {
    return itemsTree.size();  
  }
  /** 
   * Get a free color by name from the color palette pool 
   * or return a null value if the color is not available.
   * Note that the alpha channel of the returned value will
   * always be set to 255. 
   * <p>
   * This method can be used to prevent specific colors from being
   * returned by a get call (of either kind) from the color pool.
   * This method should be called with the name of each color that
   * is not wanted. The return values should be ignored.
   * 
   * @param name name of the color to be retrieved from the color pool
   * @return     the color in ARGB format 
   * @exception  NullPointerException if the color name String is null
   * @exception  NoSuchElementException if the color name is unknown
   * @see        Integer
   */
  public Integer getName(String name) {
    Integer   rgb;
    Integer   index;
    /*
     * Check the values passed by the caller
     */
    if (name == null) 
      throw new NullPointerException("Null color name value passed " + 
                                     "to get color by name routine");
    /* Check if the color name is valid or not */
    index = itemsByName.get(name);
    if (index == null)
      throw new NoSuchElementException("Unknown color name passed to " +
                                       "get color by name - " + name);
    /* 
     * Check if the color is available or not. Return null if the color
     * is not available.
     */
    if (!itemsTree.contains(index))
      return null;
    rgb = items[index].rgb;
    itemsTree.remove(index);
    return rgb | (255 << 24);
  }
  /** 
   * Get the next free color from the color palette pool or return 
   * a null value if none are available. Note that the alpha channel 
   * of the returned value will always be set to 255.
   * 
   * @return     the next color in ARGB format 
   * @see        Integer
   */
  public Integer getNext() {
    Integer   first;
    Integer   rgb;
    if (itemsTree.isEmpty())
      return null;
    first = itemsTree.first();
    rgb = items[first].rgb;
    itemsTree.remove(first);
    return rgb | (255 << 24);
  }
  /** 
   * Return a color that is currently in use, back into the color 
   * pool. The alpha value of the returned color is always ignored.
   * The color must have been obtained from the color pool. A color
   * can not be returned to the color pool more than once.  .
   *  
   * @param rgb  the color to be returned to the color pool 
   * @exception  NullPointerException if the color Integer is null 
   * @exception  NoSuchElementException if the color was never in the
   *             color pool
   * @exception  UnsupportedOperationException if the color is already 
   *             in the color pool
   * @see        Integer
   */
  public void putBack(Integer rgb) {
    Integer   index;
    /*
     * Check the values passed by the caller
     */
    if (rgb == null) 
      throw new NullPointerException("Null RGB value passed " + 
                                     "to put back routine");
    /* Strip off the alpha channel value */
    rgb &= 0xffffff;
    /* 
     * Get the index value associated with the RGB value that is 
     * going to be returned to the palette pool. If there is no
     * index value, then the RGB value is invalid. 
     */
    index = itemsByRgb.get(rgb);
    if (index == null) 
      throw new NoSuchElementException("Unknown RGB value passed to " +
                                       "put back routine - " + 
                                       String.format("0x%06x", rgb));  
    if (itemsTree.contains(index))
      throw new UnsupportedOperationException("Color value already exists in pool - " +
                                              String.format("0x%06x", rgb)); 
    itemsTree.add(index);
  }  
}
/*
 * The ColorItem class is used to keep track of one color. It is only used
 * internally by the ColorPalette class. It should not be used for any other
 * purpose.
 */
final class ColorItem {
  public final String  name;
  public final int     rgb;
  public final short   r;
  public final short   g;
  public final short   b;
  /*
   * This constructor builds an instance of a color item from
   * the color name and the RGB values
   */
  protected ColorItem(String colName, int colR, int colG, int colB) {
    name = colName;
    r = (short) colR;
    g = (short) colG;
    b = (short) colB;
    rgb = (r << 16) + (g << 8) + b;
  }
}