package org.uofm.ot.pythonAdapter;

import java.util.Map;

import org.python.core.PyDictionary;
import org.python.core.PyException;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;
import org.uofm.ot.transferobjects.DataType;
import org.uofm.ot.transferobjects.Result;



public class PythonAdapter {

	public Result executeString(String chunk,String functionName,Map<String,Object> params, DataType returntype){
		PythonInterpreter interpreter = new PythonInterpreter();

		Result resObj = new Result();


		PyDictionary dictionary = new PyDictionary();
		dictionary.putAll(params);		
		interpreter.exec(chunk);

		PyObject someFunc = interpreter.get(functionName);

		try {
			PyObject result = someFunc.__call__(dictionary);

			if(DataType.FLOAT == returntype){
				Float realResult = (Float) result.__tojava__(float.class);

				resObj.setErrorMessage("-");
				resObj.setSuccess(1);
				resObj.setResult(String.valueOf(realResult));
			} else {
				if(DataType.INT == returntype){
					int realResult = (int) result.__tojava__(int.class);
					System.out.println(realResult);

					resObj.setErrorMessage("-");
					resObj.setSuccess(1);
					resObj.setResult(String.valueOf(realResult));
				}
			}

		} catch(PyException ex){

			resObj.setErrorMessage(ex.getMessage());
			resObj.setSuccess(0);
		}

		return resObj;
	}

}
