#include "beampy.h"

PyObject* py_system(PyObject* self, PyObject* args);
PyObject* py_beam_read_product(PyObject* self, PyObject* args);
PyObject* py_beam_create_product(PyObject* self, PyObject* args);
PyObject* py_product_get_name(PyObject* self, PyObject* args);

static PyObject* beampy_error;

static PyMethodDef beampy_methods[] = {
    {"system",  py_system, METH_VARARGS, "Execute a shell command (test function)."},
    {"beam_read_product",  py_beam_read_product, METH_VARARGS, "Reads a data product."},
    {"beam_create_product",  py_beam_create_product, METH_VARARGS, "Creates a data product using an operator."},
    {"product_get_name",  py_product_get_name, METH_VARARGS, "Gets the product's name."},
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
    PyObject* m;

    m = PyModule_Create(&beampy_module);
    if (m == NULL) {
        return NULL;
	}

    beampy_error = PyErr_NewException("beampy.error", NULL, NULL);
    Py_INCREF(beampy_error);
    PyModule_AddObject(m, "error", beampy_error);

    return m;
}

/* For testing only as it doesn't need a Java VM */
static PyObject* py_system(PyObject* self, PyObject* args)
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

static PyObject* py_beam_read_product(PyObject* self, PyObject* args)
{
    const char* file_path;
    jobject product;

    if (!PyArg_ParseTuple(args, "s", &file_path)) {
        return NULL;
	}
    product = beam_read_product(file_path);

    return PyLong_FromUnsignedLongLong((unsigned PY_LONG_LONG) product);
}

static PyObject* py_beam_create_product(PyObject* self, PyObject* args)
{
    return NULL;
}

static PyObject* py_product_get_name(PyObject* self, PyObject* args)
{
    const char* product_name;
	unsigned PY_LONG_LONG product;

    if (!PyArg_ParseTuple(args, "K", &product)) {
        return NULL;
	}

    product_name = product_get_name((jobject) product);

    return PyUnicode_FromString(product_name);
}
