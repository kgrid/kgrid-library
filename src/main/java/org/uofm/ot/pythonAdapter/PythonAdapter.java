package org.uofm.ot.pythonAdapter;

import java.util.Map;

import org.apache.log4j.Logger;
import org.python.core.PyDictionary;
import org.python.core.PyException;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;
import org.uofm.ot.exception.ObjectTellerException;
import org.uofm.ot.transferobjects.DataType;
import org.uofm.ot.transferobjects.Result;



public class PythonAdapter {

	private static final Logger logger = Logger.getLogger(PythonAdapter.class);
	
	public Result executeString(String chunk,String functionName,Map<String,Object> params, DataType returntype) throws ObjectTellerException {
		PythonInterpreter interpreter = new PythonInterpreter();

		Result resObj = new Result();


		PyDictionary dictionary = new PyDictionary();
		dictionary.putAll(params);		
		interpreter.exec(chunk);

		

		try {
			PyObject someFunc = interpreter.get(functionName);
			if(someFunc != null) {
				PyObject result = someFunc.__call__(dictionary);

				if(DataType.FLOAT == returntype){
					Float realResult = (Float) result.__tojava__(float.class);

					resObj.setErrorMessage("-");
					resObj.setSuccess(1);
					resObj.setResult(String.valueOf(realResult));
				} else {
					if(DataType.INT == returntype){
						int realResult = (int) result.__tojava__(int.class);

						resObj.setErrorMessage("-");
						resObj.setSuccess(1);
						resObj.setResult(String.valueOf(realResult));
					} else {
						if(DataType.STRING == returntype) {
							String realResult = (String) result.__tojava__(String.class);
							resObj.setErrorMessage("-");
							resObj.setSuccess(1);
							resObj.setResult(realResult);
							
						} else {
							if(DataType.MAP == returntype) {
								Map<String,Object> realMap = (Map<String,Object>) result.__tojava__(Map.class);
								resObj.setErrorMessage((String)realMap.get("errorMessage"));
								resObj.setSuccess((int)realMap.get("success"));
								resObj.setResult(realMap.get("result"));
							}
						}
					}
				}
			} else {
				logger.error(functionName + " function not found in object payload ");
				ObjectTellerException exception = new ObjectTellerException(functionName + " function not found in object payload ");
				throw exception;
			}

		} catch(PyException ex){
			logger.error("Exception occured while executing python code "+ex.getMessage());
			resObj.setErrorMessage(ex.getMessage());
			resObj.setSuccess(0);
		}

		return resObj;
	}

}
