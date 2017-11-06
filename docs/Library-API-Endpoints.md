# Library API Endpoints

## Get:
#### /knowledgeObject

- returns list of knowlege objects in json with basic metadata, 200 ok on success
- optional published parameter (true/false), if set to true only returns published objects otherwise returns all objects. Default is false.

#### /knowledgeObject/ark:/{naan}/{name}
- returns a single knowledge object, 200 ok if success
- optional prefer header, if set to return=minimal then only the object metadata is returned otherwise entire object with payload and input/output messages, etc. is returned

#### /knowledgeObject/ark:/{naan}/{name}/versions
- returns a list of the versions of the object, if the object has versions, 200 ok on success
- if there are no versions or the object does not exist returns a 404

#### /knowledgeObject/ark:/{naan}/{name}/{version}
- returns a version of an object, 200 ok if success
- if the version does not exist returns a 404
- optional prefer header, if set to return=minimal then only the object metadata is returned otherwise entire object with payload and input/output messages, etc. is returned

#### /knowlegeObject/ark:/{naan}/{name}/metadata
- returns the metadata of the specified object, 200 ok if success
- if the object does not exist, returns a 404
- similar methods exist for the other children of a knowledge object:
  - /knowledgeObject/ark:/{naan}/{name}/payload
  - /knowledgeObject/ark:/{naan}/{name}/inputMessage
  - /knowledgeObject/ark:/{naan}/{name}/output
  - /knowledgeObject/ark:/{naan}/{name}/logData

## Post:

#### /knowledgeObject
- creates a new knowledge object from the body of the request in json, 201 - created result on success


#### /knowledgeObject
- creates a copy of a knowledge object with a new ark id by reference using a plaintext local ark id in the body of the request


#### /knowledgeObject/ark:/{naan}/{name}
- creates a new version of the specified object, 201 created on success
- has an optional Slug header that is used to name the version
- if no header is provided then it will use semantic versioning starting with version 0.1.0

## Put:

#### /knowledgeObject/ark:/{naan}/{name}
- edits an existing object with the specified ark id using the json body of the request, 204 on success
- creates a knowledge object at the specified ark id using the json body of the request if an object with that ark id doesn't already exist

#### /knowledgeObject/ark:/{naan}/{name}
- creates a clone of an object by fetching the object specified by the url in the plaintext body of the request
- throws a 409 conflict error if the object already exists in the local library

#### /knowledgeObject/ark:/{naan}/{name}/{published:published|unpublished}
- Toggles the published attribute of the specified object, 200 ok on success

#### /knowlegeObject/ark:/{naan}/{name}/metadata
- edits an existing metadata using the json body of the request, 204 on success
- same method exists for the other children of a knowledge object:
  - /knowledgeObject/ark:/{naan}/{name}/payload
  - /knowledgeObject/ark:/{naan}/{name}/inputMessage
  - /knowledgeObject/ark:/{naan}/{name}/outputMessage
  - /knowledgeObject/ark:/{naan}/{name}/logData

## Patch:

#### /knowledgeObject/ark:/{naan}/{name}/{version}
- reverts the base object back to the specified version, 204 on success

## Delete:

#### /knowledgeObject/ark:/{naan}/{name}
- Deletes the specified knowledge object, returns a 204 - no content on success 

#### /knowledgeObject/ark:/{naan}/{name}/{version}
- Deletes the specified version of the knowledge object, returns a 204 - no content on success

*Note: in all requests {naan}-{name} can be used in place of ark:/{naan}/{name}*
