# smith
Laboratory Information Management System (LIMS) for Illumina next-generation sequencing data

Smith stands for Sequencing Machine Information Tracking and Handling. The copyright for this acronym belongs to Yuriy Vaskin, who significantly contributed to the first version of this tool. The original publication can be found here: https://doi.org/10.1186/1471-2105-15-S14-S3. If you like and use smith, you are kindly requested to cite this paper. Smith is currently running in two research institutes and has processed nearly 100'000 samples.

The main purpose of smith is to provide information tracking for three types of data: User information, Sample information, and Sequencing run information. User information consists of the information regarding the user itself as well as the supervisor of the research group he belongs to. With this information, a user can request samples for sequencing by filling in a request form. The request form is parsed and double-checked for errors by the sequencing staff and imported into the smith database. 

When a pool of samples is sequenced, the staff assign a lane to the sample pool and prepare a sequencing run form, which is also uploaded to the database. With this information, smith scans the runfolders at regular intervals for finished runs and triggers downstream analyses, such as demultiplexing. When the downstream analyses are finished, smith can send out email communications to the sample owners (optional).

Smith is a JavaEE web application making use of Java Server Faces and PrimeFaces frameworks. It uses Hibernate 4.3.1 for object-relations-management and a MySQL backend database.

Thanks for your interest in smith.

The developers
