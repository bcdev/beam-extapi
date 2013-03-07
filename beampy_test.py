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

'''
pis = p.createPixelInfoString(100,100)
print("pis =", pis)


for i in range(10000):
    band_names = p.getBandNames();
    print("band_names =", band_names)

desc = p.getDescription()
print("desc =", desc)

name = p.getName()
print("name =", name)

'''