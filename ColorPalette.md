# Introduction #

The ColorPalette class manages color pools allowing Processing applications (actually any Java application) to obtains colors (next free color or a color by name), use them, and then return them to the pool.


# Details #

The ColorPalette class manages color pools. The basic idea is that the ColorPalette class maintains a pool of unique colors (no duplicates). Each color in the pool has a name (from web standards) and an RGB value. Once a color has been pulled from the pool, it is deemed to be in use (busy) and will not returned again to a caller, until the original user return the color to the pool.