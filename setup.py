#!/usr/bin/env python3

import sys
import os
import platform
from distutils.core import setup
from distutils.extension import Extension

# JDK_HOME = '/home/marta/jdk1.6.0_35'
JDK_HOME = os.environ.get('JDK_HOME', None)
if JDK_HOME is None:
    print('Error: Environment variable "JDK_HOME" must be set to a JDK (>= v1.6) installation directory')
    exit(1)

if platform.system() == 'Windows' and os.environ.get('VS90COMNTOOLS', None) is None:
    print('Note: If you get an error saying "Unable to find vcvarsall.bat",')
    print('      you may need to set environment variable VS90COMNTOOLS.')
    print('      If you use Visual Studio 10, then: SET VS90COMNTOOLS=%VS100COMNTOOLS%,')
    print('      if you use Visual Studio 12, then: SET VS90COMNTOOLS=%VS110COMNTOOLS%.')

if sys.argv[1] == 'install':
    print('Note: In order to use the "beampy" module, you need to set BEAM_HOME')
    print('      to a valid BEAM (>= v4.11) installation directory.')


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
                                    include_dirs=['src/main/c',
                                                  'src/main/c/gen',
                                                  JDK_HOME + '/include',
                                                  JDK_HOME + '/include/linux',
                                                  JDK_HOME + '/include/win32'],
                                    library_dirs=[JDK_HOME + '/jre/lib/i386/server',
                                                  JDK_HOME + '/lib'],
                                    libraries=['jvm'],
                                    define_macros=[('WIN32','1')]
                          )]
)
