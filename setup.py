#!/usr/bin/env python3

from distutils.core import setup
from distutils.extension import Extension

JDK_HOME = '/home/marta/jdk1.6.0_35'

setup(name             = 'beampy', 
      description      = 'BEAM Python API',
      long_description = 'A framework for developing applications for Earth Observation data based on the BEAM (Java) API',
      version          = '4.11.0',
      platforms        = 'Python 3, Java 1.6, BEAM 4.11',
      author           = 'Brockmann Consult GmbH',
      author_email     = 'beam@brockmann-consult.de',
      maintainer       = 'Brockmann Consult GmbH',
      maintainer_email = 'beam@brockmann-consult.de',
      license          = 'GPL 3',
      url              = 'http://www.brockmann-consult.de/beam/',
      download_url     = 'http://www.brockmann-consult.de/beam/',
      py_modules       = ['beampy'],
      ext_modules      = [Extension('_beampy', 
                                    ['src/main/c/beam_util.c',
                                     'src/main/c/beampy_carray.c',
                                     'src/main/c/gen/beam_capi.c',
                                     'src/main/c/gen/beampy.c'
                                    ],                                 
                                    include_dirs=['src/main/c/gen',
                                                  JDK_HOME + '/include',
                                                  JDK_HOME + '/include/linux'],
                                    library_dirs=[JDK_HOME + '/jre/lib/i386/server'],
                                    libraries=['jvm'],
                                    define_macros=[('_DEBUG', '1')]
                          )]
)
