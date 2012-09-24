#include "beampy.h"

PyObject* beampy_system(PyObject* self, PyObject* args);
PyObject* beampy_read_product(PyObject* self, PyObject* args);
PyObject* beampy_create_product(PyObject* self, PyObject* args);

static PyObject* beampy_error;

static PyMethodDef beampy_methods[] = {
    {"system",  beampy_system, METH_VARARGS, "Execute a shell command (test function)."},
    {"read_product",  beampy_read_product, METH_VARARGS, "Reads a data product."},
    {"create_product",  beampy_create_product, METH_VARARGS, "Reads a data product."},
    {NULL, NULL, 0, NULL}  /* Sentinel */
};

static struct PyModuleDef beampy_module = {
   PyModuleDef_HEAD_INIT,
   "beampy",    /* name of module */
   "BEAM/Python API",  /* module documentation, may be NULL */
   -1,          /* size of per-interpreter state of the module, or -1 if the module keeps state in global variables. */
   beampy_methods
};

PyMODINIT_FUNC PyInit_beampy(void) 
{
    PyObject *m;

    m = PyModule_Create(&beampy_module);
    if (m == NULL)
        return NULL;

    beampy_error = PyErr_NewException("beampy.error", NULL, NULL);
    Py_INCREF(beampy_error);
    PyModule_AddObject(m, "error", beampy_error);

    return m;
}


static PyObject* beampy_system(PyObject* self, PyObject* args)
{
    const char* command;
    int sts;

    if (!PyArg_ParseTuple(args, "s", &command)) {
        return NULL;
	}
    sts = system(command);
    if (sts < 0) {
        PyErr_SetString(beampy_error, "System command failed");
        return NULL;
    }
    return PyLong_FromLong(sts);
}

static PyObject* beampy_read_product(PyObject* self, PyObject* args)
{
    const char* file_path;
    jobject product;

    if (!PyArg_ParseTuple(args, "s", &file_path)) {
        return NULL;
	}
    product = beamc_read_product(file_path);

    return PyLong_FromUnsignedLongLong((unsigned long long) product);
}

static PyObject* beampy_get_product_name(PyObject* self, PyObject* args)
{
    const char* file_path;
    jobject product;
	unsigned PY_LONG_LONG product_p;

    if (!PyArg_ParseTuple(args, "K", &product_p)) {
        return NULL;
	}


    product = beamc_get_product_name((jobject) product_p);
    return PyLong_FromUnsignedLongLong((unsigned long long) product);
}


static PyObject* beampy_create_product(PyObject* self, PyObject* args)
{
    return NULL;
}

