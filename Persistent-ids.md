
# The Object Teller and the Fedora repository

The Object Teller is a front end for the Fedora commons repositories used to store Knowledge Objects (KOs) as part of the Knowledge Grid (K-grid). The Object Teller application is responsible for:

- Managing the lifecycle of KOs (create, update, configure, publish, delete)
- Enabling discovery of KOs (view, filter, search, import/export, maybe proxy)
- Controlling access to KOs for object owners, informaticians/librarians, and library users (using a combination of simple role-based access control and discretionary access control)
- Providing an (restful) API usable by clients of the K-grid (object management and object execution)
- Providing an execution stack for certain types of KOs (Python, simple OWL)
- We are using instances of Fedora repositories as the back-end data store for knowledge objects and associated information. The Object Teller front end should interoperate reasonably well with any Fedora repository instance, with minimal configuration and setup. So far we are not really exposing the underlying repository (all use cases go through the Object Teller front-end.

# The problem

There may be multiple versions of a particular general bit of knowledge encapsulated in more than one KO, or a single KO with multiple versions, and they may be may be under different names and owners, either public and private, and in more than one instance of a K-grid repository.

The Knowledge Grid is inherently distributed. Both the management and the use of KOs is carried out by a diverse set of authors, and clients, human and machine.

Each of these individual KOs (and the bit of knowledge encapsulated) has an identity in the world because it was created and is maintained by some person or organization for some ongoing purpose. The problem is two-fold: we must be able to find (and find again) a KO of interest, and we must be able to tell that it is, or is not, more or less definitively, the same as some other KO.

To that end we want to assign an identifier.

## Internal identifiers

Because no one group or application controls the lifecycle of every instance of every KO (we expect there to be many libraries and many knowledge creators) we are not primarily interested in specifying the internal identifiers used by various repositories.

The internal identifiers must be sufficient for implementing the use cases supported by the implementing platform, but need not be exposed (much) to users of the system. (In a relational database, for example, it's good practice to make use of synthetic keys that are generated internally by the database engine.)

## External (entity/resource) identifiers

For external ids, which must be unique across libraries, we have a choice of natural (business) keys, or synthetic (surrogate) keys. [1][]

An example of a platform that uses a global natural key is Maven, a repository for binary artifacts, primarily code. The backing repo, in Maven's case, is a simple file system, hierarchical, with conventions around the names of the nodes and the binaries (and associated metadata and variant files). Each artifact (along with it's associated metadata/variants files) is uniquely located using "coordinates" consisting of a group id, artifact id, and version.

These three metadata attributes form a unique natural key that enables discovery, hierarchical grouping, and versioning, are human readable, and easily associated with other systems (like source code repositories) and organizations. The attributes forming the unique natural key are chosen by the creator of the artifact, conventionally the group id is a reversed domain, artifact id is a simple name, and the version string follows a prescribed major.minor.patch format. Attributes are not always chosen wisely, and organizations and code bases change regularly, so sometimes it's difficult to track the provenance and variations of an artifact.

Nonetheless, the artifacts are uniquely identified and the repositories are easy to navigate and replicate.

# Ko use cases involved or impacted by persistent ID strategy

1. Create object
* needs a new identifier

2. Edit/update object 
* might need to update metadata/version in an external source

3. Delete a KO
* probably need to notify the persistent ID store
* may need to keep track of other KOs referring to or cloning from this persistent id

4. Publish/Unpublish a KO (public/private)
* again may need to notify or update an external source
* and keep track of references

5. Find a particular KO
* Probably accept only persistent id regardless of internal id

6. Import/Export, and other library-to-library use cases
* depend on a consistent persistent id across libraries

Background material from the California Digital Library on the ezid service and ark: IDs (our target solution)

More about ezid accounts - http://ezid.cdlib.org/learn/id_basics

The ezid.cdlib.org API - http://ezid.cdlib.org/doc/apidoc.html

Info about the noid python module and how to run it locally - http://www.cdlib.org/inside/diglib/ark/noid.pdf

Java wrapper classes for talking to ezid or a local minter (for illustration purposes, not for copy/paste) - https://github.com/pulibrary/arkifier

[1]: For example, I am represented in countless databases and repositories by one or more of my email addresses. On the other hand, some ask me to choose an id, which could be a random string for all the system cares. My email is then associated with the entity identified by that random string. The difference is that if my email changes the system can simply update it, rather than having to change the underlying key which has been used throughout the system in ways that complicate the update. IS my email permanent and unique? Is my SSN? Is my thumbprint? My DNA? Or are those attributes of the real me, which might as well be identified as 06HR$jkj&%ghi007... just call me "007" for short.




Persistent ID proposal
Summary
Proposal for persistent (external) ID generation, management, and use in the knowledge grid, and updates to the knowledge object model in fedora, and the knowledge object model and API in Object Teller front-end, to accommodate.



The basics
Use ark: ids
Use ezid.cdlib.org to mint ids
Use fedora internal ids for knowledge objects/containers
Use n2t.net as the root name mapping authority (resolver) so that n2t.net/ark:/... redirects to a KO in a K-grid instance
Every K-grid instance is an NMA, too, that knows how to map ark: ids to it's own KOs
Only one library can "own" an object (the creating library, but can transfer ownership within/between libraries). That's the local URL set as target at ezid.
Changing and (re)publishing an existing KO probably requires at least a version change, and may require a new ark: id. More will be revealed.


The create/update lifecycle
On creating a knowledge object
– create the KO as private
– create an ark: id, in the reserved state, via ezid
– update the entry at ezid with the KO's local URL (establishes "ownership")
On updates to the KO
– update the local KO
– KO should be private/unpublished, otherwise "republish" to the ezid entry with relevant metadata


Publishing/unpublishing a KO
On publishing a KO (assuming you are the owner)
– update the ezid entry with relevant metadata
– update the ezid entry to public (updates n2t.org)
– make the KO public in the local library
On unpublishing a KO (assuming you are the owner)
– this means the ezid target is you, but beware, others may hold references or have copies, so
– mark it unavailable at ezid and add a note as to what happened
– make the KO private in the local library
– consider letting everyone who uses or has cloned the KO know it's gone (how?)
Note: there is far more to the publish workflow, this is just the basic id management portion



Fetching a KO
If found locally (using the ark: id) return the object
If not found locally,
– either query n2t.org for a new URL (maybe in some other K-grid instance?) and try to get it
– or send them on the n2t.org and hope for the best!


Deleting a KO
First make sure it's unpublished
If it's never been published
– the ark: id is reserved and no one else will be relying on it, so
– delete it at ezid
– delete it in the K-grid. Bye-bye.
If it has been published previously and you are not the owner
– the ezid target URL is not you, and others may hold references or have copies, so,
– delete the KO locally
– consider letting everyone who uses your copy the KO know it's gone (how?)
If it has been published previously and you are the owner
– the ezid target is you, but others may still hold references or have copies, so
– mark it unavailable at ezid and add a note as to what happened
– delete the KO locally
– consider letting everyone who uses or has copied the KO know it's gone (how?)


Transferring ownership of a KO
Update the ezid entry with a new target URL (updates n2t.org)

Here are some examples and questions and answers.
