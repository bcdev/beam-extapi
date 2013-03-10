set BEAM_HOME=C:\Users\Norman\JavaProjects\beam\beam
set BEAM_VERSION=4.10.3

set CERES_HOME=C:\Users\Norman\JavaProjects\beam\ceres
set CERES_VERSION=0.13.2-SNAPSHOT

javah -classpath %CERES_HOME%\ceres-core\target\classes;%BEAM_HOME%\modules\beam-gpf-%BEAM_VERSION%;%BEAM_HOME%\modules\beam-core-%BEAM_VERSION%;target/classes  -d src/main/c/gen org.esa.beam.extapi.COperator org.esa.beam.extapi.CApi org.esa.beam.extapi.PythonApi