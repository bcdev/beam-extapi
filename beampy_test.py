import sys
import beampy


dir(beampy)

if len(sys.argv) != 2:
    printf("usage: %s <file>", sys.argv[0]);
    sys.exit(1)

p = beampy.ProductIO.readProduct(sys.argv[1])
print("p =", p)

w = p.getSceneRasterWidth()
print("w =", w)

h = p.getSceneRasterHeight()
print("h =", h)

name = p.getName()
print("name = {!s}\n".format(name))

desc = p.getDescription()
print("desc =", desc)

band_names = p.getBandNames();
print("band_names =", band_names)

'''
pis = p.createPixelInfoString(100,100)
print("pis =", pis)



name = p.getName()
print("name =", name)

'''