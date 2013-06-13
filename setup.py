#!/usr/bin/env python3

import sys
import os
import platform
from distutils.core import setup
from distutils.extension import Extension

# e.g. JDK_HOME = '/home/marta/jdk1.7.0_15'
JDK_HOME = os.environ.get('JDK_HOME', None)
if JDK_HOME is None:
    print('Error: Environment variable "JDK_HOME" must be set to a JDK (>= v1.6) installation directory')
    exit(1)

IS64 = sys.maxsize > 2 ** 32
WIN32 = platform.system() == 'Windows'
LINUX = platform.system() == 'Linux'
DARWIN = platform.system() == 'Darwin'
print('Building a %s-bit library for a %s system' % ('64' if IS64 else '32', platform.system()))

if WIN32 and os.environ.get('VS90COMNTOOLS', None) is None:
    print('Note: If you get an error saying "Unable to find vcvarsall.bat",')
    print('      you may need to set environment variable VS90COMNTOOLS.')
    print('      If you use Visual Studio 2011, then: SET VS90COMNTOOLS=%VS100COMNTOOLS%,')
    print('      if you use Visual Studio 2012, then: SET VS90COMNTOOLS=%VS110COMNTOOLS%.')

if len(sys.argv) > 1 and sys.argv[1] == 'install':
    print('Note: In order to use the "beampy" module, you need to set BEAM_HOME')
    print('      to a valid BEAM (>= v4.11) installation directory.')
    print('      Currently, BEAM_HOME =', os.environ.get('BEAM_HOME', None))

sources = ['src/main/c/beam_util.c',
           #'src/main/c/beam_jvm.c',
           'src/main/c/beampy_carray.c',
           'src/main/c/gen/beam_capi.c',
           'src/main/c/gen/beampy.c']
include_dirs = ['src/main/c',
                'src/main/c/gen']
library_dirs = []
libraries = []
define_macros = []
extra_link_args = []
extra_compile_args = []

if WIN32:
    define_macros += [('WIN32', '1')]
    include_dirs += [JDK_HOME + '/include', JDK_HOME + '/include/win32']
    libraries=['jvm']
    library_dirs = [JDK_HOME + '/jre/lib/i386/server',
                    JDK_HOME + '/lib']

if LINUX:
    include_dirs += [JDK_HOME + '/include', JDK_HOME + '/include/linux']
    libraries=['jvm']
    library_dirs = [JDK_HOME + '/jre/lib/i386/server',
                    JDK_HOME + '/lib']

if DARWIN:
    include_dirs += [JDK_HOME + '/Headers']
    libraries=['server']
    library_dirs = [JDK_HOME + '/Libraries']
    extra_link_args += ['-framework JavaVM']
    extra_compile_args += ['-framework JavaVM']
    JDK_HOME = '/System/Library/Frameworks/JavaVM.framework'
    BEAM_HOME = '/Applications/beam-4.11'
    # todo - adapt settings for Mac OS X, JDK_HOME dir seems to have different structure
    # see http://docs.python.org/3.2/distutils/setupscript.html
    # see http://docs.python.org/3.2/distutils/apiref.html?highlight=setup#distutils.core.setup
    # sudo ln -s /System/Library/Java/JavaVirtualMachines/1.6.0.jdk/Contents/Libraries/libclient64.dylib /usr/lib/libclient.dylib
    

setup(name='beampy',
    description='BEAM Python API',
    long_description='A framework for developing applications for Earth Observation data based on the BEAM (Java) API',
    version='4.11.0',
    platforms='Python 3, Java 1.6, BEAM 4.11',
    author='Brockmann Consult GmbH',
    author_email='beam@brockmann-consult.de',
    maintainer='Brockmann Consult GmbH',
    maintainer_email='beam@brockmann-consult.de',
    license='GPL 3',
    url='http://www.brockmann-consult.de/beam/',
    download_url='http://www.brockmann-consult.de/beam/',
    py_modules=['beampy'],
    ext_modules=[Extension('_beampy',
        sources,
        include_dirs=include_dirs,
        library_dirs=library_dirs,
        libraries=libraries,
        extra_link_args=extra_link_args,
        extra_compile_args=extra_compile_args,
        define_macros=define_macros
    )]
)
