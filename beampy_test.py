import beampy

dir(beampy)

p = beampy.ProductIO.readProduct("C:\EOData\MERIS\MER_RR__1alpen.N1")
print("p =", p)

w = p.getSceneRasterWidth()
print("w =", w)

h = p.getSceneRasterHeight()
print("h =", h)

name = p.getName()
print("name =", name)

