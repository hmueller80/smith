import sys
import os
from os import listdir
from os.path import isfile
import errno
import logging
from optparse import OptionParser
from bioblend.galaxy import GalaxyInstance
from bioblend.galaxy.histories import HistoryClient
from bioblend.galaxy.tools import ToolClient
from bioblend.galaxy.workflows import WorkflowClient
from bioblend.galaxy.datasets import DatasetClient
from bioblend.galaxy.datasets import DatasetStateException
from bioblend.galaxy.datasets import DatasetTimeoutException
from bioblend.galaxy.genomes import GenomeClient
from bioblend.galaxy.libraries import LibraryClient

#Execute workflows from the command line.
#Example calls:
#python smith_run_workflow.py --reads='/Users/yvaskin/Dropbox/Old/tmp1/gears/reads/e_coli_1000.fq' --bam='/Users/yvaskin/Documents/project/sandbox/smith_run_tests/bam_base' --bigWig='/Users/yvaskin/Documents/project/sandbox/smith_run_tests/bigWig_base' --genome='hg19' --workflow='chipseq'
#python smith_run_workflow.py --reads=/data/Illumina/PublicData/FASTQ/140418_SN880_0265_AC3VLNACXX/Project_gdellino/Sample_PR9_H3me1K4_S9332/PR9_H3me1K4_S9332_CTATGC_L008_R1_001.fastq.gz,/data/Illumina/PublicData/FASTQ/140418_SN880_0265_AC3VLNACXX/Project_gdellino/Sample_PR9_H3me1K4_S9332/PR9_H3me1K4_S9332_CTATGC_L008_R1_002.fastq.gz,/data/Illumina/PublicData/FASTQ/140418_SN880_0265_AC3VLNACXX/Project_gdellino/Sample_PR9_H3me1K4_S9332/PR9_H3me1K4_S9332_CTATGC_L008_R1_003.fastq.gz,/data/Illumina/PublicData/FASTQ/140418_SN880_0265_AC3VLNACXX/Project_gdellino/Sample_PR9_H3me1K4_S9332/PR9_H3me1K4_S9332_CTATGC_L008_R1_004.fastq.gz,/data/Illumina/PublicData/FASTQ/140418_SN880_0265_AC3VLNACXX/Project_gdellino/Sample_PR9_H3me1K4_S9332/PR9_H3me1K4_S9332_CTATGC_L008_R1_005.fastq.gz,/data/Illumina/PublicData/FASTQ/140418_SN880_0265_AC3VLNACXX/Project_gdellino/Sample_PR9_H3me1K4_S9332/PR9_H3me1K4_S9332_CTATGC_L008_R1_006.fastq.gz,/data/Illumina/PublicData/FASTQ/140418_SN880_0265_AC3VLNACXX/Project_gdellino/Sample_PR9_H3me1K4_S9332/PR9_H3me1K4_S9332_CTATGC_L008_R1_007.fastq.gz,/data/Illumina/PublicData/FASTQ/140418_SN880_0265_AC3VLNACXX/Project_gdellino/Sample_PR9_H3me1K4_S9332/PR9_H3me1K4_S9332_CTATGC_L008_R1_008.fastq.gz,/data/Illumina/PublicData/FASTQ/140418_SN880_0265_AC3VLNACXX/Project_gdellino/Sample_PR9_H3me1K4_S9332/PR9_H3me1K4_S9332_CTATGC_L008_R1_009.fastq.gz --bam=/home/yvaskin/smith/test_data/BAM/140418_SN880_0265_AC3VLNACXX/Project_gdellino/Sample_PR9_H3me1K4_S9332/bam --bigWig=/home/yvaskin/smith/test_data/BED/140418_SN880_0265_AC3VLNACXX/Project_gdellino/Sample_PR9_H3me1K4_S9332/bigWig --genome=hg19 --workflow=chipseq
#--reads=/Users/yvaskin/Documents/project/sandbox/trash/hg19_1000.fastq --mates=/Users/yvaskin/Documents/project/sandbox/trash/hg19_1000_1.fastq --bam=/Users/yvaskin/Documents/project/sandbox/smith_run_tests/bam_base --bigWig=/Users/yvaskin/Documents/project/sandbox/smith_run_tests/bigWig_base --genome=hg19 --workflow=chipseq
#--reads=/data/Illumina/PublicData/FASTQ/140418_SN880_0265_AC3VLNACXX/Project_gdellino/Sample_PR9_H3me1K4_S9332/PR9_H3me1K4_S9332_CTATGC_L008_R1_001.fastq.gz --bam=/home/yvaskin/smith/test_data/BAM/140418_SN880_0265_AC3VLNACXX/Project_gdellino/Sample_PR9_H3me1K4_S9332/bam --bigWig=/home/yvaskin/smith/test_data/BED/140418_SN880_0265_AC3VLNACXX/Project_gdellino/Sample_PR9_H3me1K4_S9332/bigWig --genome=hg19 --workflow=chipseq
#--reads=/home/yvaskin/test_data/hg19_1000.fastq --bam=/home/yvaskin/smith/test_data/Temp/BAM/hg1000 --bigWig=/home/yvaskin/smith/test_data/Temp/BigWig/hg1000 --genome=hg19 --workflow=chipseq



################################################################################
#Configs
from setuptools.command.setopt import option_base

GALAXY_URL = 'http://blade-b1-p2.iit.ieo.eu:8080'
API_KEY = '172ac3b6c9e2e70e03f09fc2dc04c1e3'
SEPARATOR = ','
DOWNLOAD_ATTEMPTS = 2


WORKFLOW_IDS = {
    'ChIP-Seq' : '5969b1f7201f12ae',
    'RNA-Seq' : 'a799d38679e985db',
    'DNA-Seq' : 'df7a1f0c02a5b08e',
}

TOOL_IDS = {
    'ChIP-Seq' : 'gears_tool',
    'RNA-Seq' : 'gears_tool_rna',
    'DNA-Seq' : 'gears_tool_dna',
}


GENOME_PATH_BWA = {
    'hg19': '/home/hmuller/db/bwa/hg19/hg19.fa',
    'mm9': '/home/hmuller/db/bwa/mm9/mm9.fa',
    'mm10': '/home/hmuller/db/bwa/mm10/mm10.fa',
    'danRer7': '/home/hmuller/db/bwa/danRer7/danRer7.fa'
}

GENOME_PATH_BOWTIE1 = {
    'test': '/home/yvaskin/galaxy/tests/rna/test_data',
    'hg18': '/home/hmuller/db/bowtie/hg18',
    'hg19': '/home/hmuller/db/bowtie/hg19',
    'mm9': '/home/hmuller/db/bowtie/mm9',
    'mm10': '/home/hmuller/db/bowtie/mm10',
    'oryCun2': '/home/hmuller/db/bowtie/oryCun2',
}

################################################################################
#Utils
def get_genome_path(genome, worflow_type):
    if worflow_type == 'ChIP-Seq' or worflow_type == 'DNA-Seq':
        return GENOME_PATH_BWA[genome]

    elif worflow_type == 'RNA-Seq':
        return GENOME_PATH_BOWTIE1[genome]

    return GENOME_PATH_BWA[genome]

def create_folder(outpath):
    if '/' in outpath:
        outpath = outpath[:outpath.rfind('/')]
        try:
            os.makedirs(outpath)
        except OSError as exc:  # Python >2.5
            if exc.errno == errno.EEXIST and os.path.isdir(outpath):
                pass
            else:
                raise


def find_dataset_id_by_extention(datasetClient, output, ext):
    id = ''
    for dataset_id in output['outputs']:
        dataset = datasetClient.show_dataset(dataset_id)
        if dataset['file_ext'] == ext:
            id = dataset_id
            print 'SHOW_DOWNLOADED_DATASET'
            print dataset
            print ext
            break
    return id


def download_dataset(datasetClient, datasetId, outpath):
    if datasetId != '':
        create_folder(outpath)
        for i in range(1, DOWNLOAD_ATTEMPTS):
            try:
                datasetClient.download_dataset(datasetId, outpath, False, True)
                break
            except DatasetStateException as dse:
                print dse
                dataset = datasetClient.show_dataset(datasetId)
                print "NEXT ATTEMPT TO DOWNLOAD %s" %dataset
                continue
            except DatasetTimeoutException as dte:
                print dte
                dataset = datasetClient.show_dataset(datasetId)
                print "NEXT ATTEMPT TO DOWNLOAD %s" %dataset
                continue

    else:
        print 'Dataset id %s not found. Fail to download dataset to % s.' % (datasetId, outpath)


################################################################################
#Parameters
def prepare_optparser():
    """Prepare optparser object. New options will be added in this
    function first.
    """
    usage = """usage: %prog [-f folder] [-m SR|PE] [-a bam_base] [-p bigWig_base] [-b bed_base] [-g genome] [-w workflow]

    """
    description = "%prog -- Runs galaxy NGS workflows"

    optparser = OptionParser(version="%prog 1.0", description=description, usage=usage, add_help_option=False)
    optparser.add_option("-h", "--help", action="help", help="show this help message and exit.")
    optparser.add_option("-f", "--folder", dest="folder", type="string",
                         help="Folder with fastq",
                         default="")
    optparser.add_option("-m", "--mode", dest="mode", type="string",
                         help="Read mode",
                         default="SR")
    optparser.add_option("-a", "--bam", dest="bam", type="string",
                         help="BAM base path",
                         default="")
    optparser.add_option("-p", "--bigWig", dest="bigWig", type="string",
                         help="BigWig base path",
                         default="")
    optparser.add_option("-b", "--bed", dest="bed", type="string",
                         help="bed base path",
                         default="")
    optparser.add_option("-g", "--genome", dest="genome", type="string",
                         help="Reference genome",
                         default="")
    optparser.add_option("-w", "--workflow", dest="workflow", type="string",
                         help="Workflow type",
                         default="")
    optparser.add_option("--verbose", dest="verbose", type="int", default=2,
                         help="Set verbose level. 0: only show critical message, 1: show additional warning message, 2: show process information, 3: show debug messages. DEFAULT:2")
    return optparser


def check_len(arg, name):
    if len(arg) == 0:
        logging.error("Parameter is empty: %s!" % name)
        sys.exit(1)

def set_reads(options):
    options.reads = ''
    options.mates = ''

    if not os.path.isdir(options.folder):
        logging.error("Not a folder: %s!" % options.folder)
        sys.exit(1)

    if options.mode == 'PE':
        names =  [ f[:-9] for f in listdir(options.folder) if f.endswith('.fastq.gz')]
        reads = []
        mates = []
        for nameR1 in names:
            if nameR1 in reads or nameR1 in mates:
                continue
            nameR2 = nameR1
            if  '_R1_' in nameR1:
                nameR2 = nameR1.replace('_R1_', '_R2_')
            else:
                nameR1 = nameR2.replace('_R2_', '_R1_')
            if nameR1 in names and nameR2 in names:
                reads.append(nameR1)
                mates.append(nameR2)
        options.reads = ','.join([options.folder + n + '.fastq.gz' for n in reads])
        options.mates =','.join([options.folder + n + '.fastq.gz' for n in mates])
    else:
        options.reads = ','.join([ options.folder + f for f in listdir(options.folder) if f.endswith('.fastq.gz')])
    return options


def opt_validate(optparser):
    """Validate options from a OptParser object.

    Ret: Validated options object.
    """
    (options, args) = optparser.parse_args()
    check_len(options.mode, 'mode')
    check_len(options.folder, 'folder')

    options = set_reads(options)

    # inputs
    check_len(options.reads, 'reads')
    check_len(options.bam, 'bam')
    check_len(options.bigWig, 'bigWig')
    check_len(options.bed, 'bed')
    check_len(options.genome, 'genome')
    check_len(options.workflow, 'workflow')

    #types
    if options.workflow not in WORKFLOW_IDS.keys() or options.workflow not in TOOL_IDS.keys():
        logging.error("Unsupported workflow type: %s. Possible types are: %s!" % (options.workflow, WORKFLOW_IDS.keys()))
        sys.exit(1)

    #pairs
    if len(options.mates) != 0:
        reads = [read for read in str(options.reads).split(SEPARATOR) if len(read) != 0]
        mates = [read for read in str(options.mates).split(SEPARATOR) if len(read) != 0]
        if len(reads) != len(mates):
            logging.error("Reads and mates have different number of paths: %s and %s" % (len(reads), len(mates)))
            sys.exit(1)

    #genome
    if options.genome not in GENOME_PATH_BWA and options.genome not in GENOME_PATH_BOWTIE1:
        logging.error("Unsupported genome: %s. Possible types are: %s!" % (options.genome, GENOME_PATH_BOWTIE1.keys() + GENOME_PATH_BWA.keys() ))
        sys.exit(1)

    logging.basicConfig(level=(4 - options.verbose) * 10,
                        format='%(levelname)-5s @ %(asctime)s: %(message)s ',
                        datefmt='%a, %d %b %Y %H:%M:%S',
                        stream=sys.stderr,
                        filemode="w"
    )

    options.error = logging.critical  # function alias
    options.warn = logging.warning
    options.debug = logging.debug
    options.info = logging.info

    return options

################################################################################
#Runner class
class GalaxyRunner:
    def __init__(self, options):
        self.reads = [read for read in str(options.reads).split(SEPARATOR) if len(read) != 0]
        self.mates = [read for read in str(options.mates).split(SEPARATOR) if len(read) != 0]
        self.genome = options.genome
        self.bigWig = options.bigWig
        self.bed = options.bed
        self.bamBase = options.bam
        self.workflowType = options.workflow

        self.info  =  options.info
        self.warn  =  options.warn
        self.debug = options.debug
        self.error = options.error

        self.galaxyInstance = GalaxyInstance(url=GALAXY_URL, key=API_KEY)
        self.historyClient = HistoryClient(self.galaxyInstance)
        self.toolClient = ToolClient(self.galaxyInstance)
        self.workflowClient = WorkflowClient(self.galaxyInstance)
        self.datasetClient = DatasetClient(self.galaxyInstance)
        self.genomeClient = GenomeClient(self.galaxyInstance)
        self.libraryClient = LibraryClient(self.galaxyInstance)

        self.history = self.historyClient.create_history('smith')
        self.library = self.libraryClient.create_library("smith")


    def clear (self):
        #delete history
        #self.historyClient.delete_history(self.history['id'])
        #if galaxy instance support dataset purging
        self.historyClient.delete_history(self.history['id'], True)
        self.libraryClient.delete_library(self.library['id'])

    def is_paired(self):
        return len(self.mates) != 0

    def library_run(self):
        print self.library
        for read in self.reads:
            #output = self.libraryClient.upload_file_from_url(self.library['id'], read)
            output = self.libraryClient.upload_file_from_local_path(self.library['id'], read)
            print output
            historyDs = self.historyClient.upload_dataset_from_library(self.history['id'], output[0]['id'])
            print historyDs
        self.libraryClient.delete_library(self.library['id'])

    def run(self):
        uploadedReads = self.upload_reads(self.reads)
        uploadedMates = self.upload_reads(self.mates)

        datasetmaps = self.get_datasets(uploadedReads, uploadedMates)

        #hack: use urls of files to access the datasets
        read_urls, mate_urls = self.get_galaxy_urls(uploadedReads, uploadedMates)
        print read_urls

        params = {}

        params = {TOOL_IDS[self.workflowType]: {'reference_genome' : get_genome_path(self.genome, self.workflowType)}}
        params[TOOL_IDS[self.workflowType]]['genome_assembly'] = self.genome
        params[TOOL_IDS[self.workflowType]]['in_path'] = read_urls
        if self.is_paired():
            params[TOOL_IDS[self.workflowType]]['in_path_mate'] = mate_urls


        #hack: run for the first dataset and pass all the paths as the parameter
        dataset_map = datasetmaps[0]
        print "DATASET"
        print dataset_map


        output = self.workflowClient.run_workflow(WORKFLOW_IDS[self.workflowType],
                                              dataset_map, params, self.history['id'])
        print "OUTPUT"
        print output
        print "SHOW_DATASET"
        print self.datasetClient.show_dataset(output['outputs'][0])
        #print self.datasetClient.show_dataset(output)
        #outputs.append(output)
        self.download_results(output)


    def get_datasets(self, uploadedReads, uploadedMates):
        workflow = self.workflowClient.show_workflow(WORKFLOW_IDS[self.workflowType])
        datasetmaps = []

        readlen = len(uploadedReads)
        for i in range(0, readlen):
            print "UPLOADED_READS"
            print uploadedReads[i]
            #print self.datasetClient.show_dataset(uploadedReads[i])
            dataset_map = {workflow['inputs'].keys()[0]: {'id': uploadedReads[i]['id'], 'src': 'hda'}}
            #if self.is_paired():
                #dataset_map[workflow['inputs'].keys()[1]] = {'id': uploadedMates[i]['id'], 'src': 'hda'}

            datasetmaps.append(dataset_map)

        return datasetmaps

    def get_file_name(self, uploaded_read):
        ds = self.datasetClient.show_dataset(uploaded_read['id'])
        if 'file_name' not in ds:
            self.error("No file_name tag for a dataset")
            return ''
        return ds['file_name']

    def get_galaxy_urls(self, uploadedReads, uploadedMates):
        read_urls = ''
        mate_urls = ''
        read_len = len(uploadedReads)
        for i in range(0, read_len):
            name = self.get_file_name(uploadedReads[i])
            if name == '':
                continue
            if read_urls != '':
                read_urls += SEPARATOR
            read_urls += name

            if self.is_paired():
                mate_name = self.get_file_name(uploadedMates[i])
                if mate_name != '':
                    if mate_urls != '':
                        mate_urls += SEPARATOR
                    mate_urls += mate_name

        return read_urls, mate_urls


    def upload_reads(self, reads):
        uploaded_reads = []
        print "READS"
        print reads
        for read in reads:
            #library_output = self.libraryClient.upload_file_from_local_path(self.library['id'], read)
            #print library_output
            #output = self.historyClient.upload_dataset_from_library(self.history['id'], library_output[0]['id'])
            #print output
            output = self.toolClient.upload_file(read, self.history['id'])

            print 'SHOW_UPLOADED'
            output = self.historyClient.show_dataset( self.history['id'], output['outputs'][0]['id'])
            #output = self.historyClient.show_dataset( self.history['id'], output['id'])
            print output
            uploaded_reads.append(output)
        return uploaded_reads

    def download_results (self, output):
        download_dataset(self.datasetClient, find_dataset_id_by_extention(self.datasetClient, output, 'bigWig'), self.bigWig)
        download_dataset(self.datasetClient, find_dataset_id_by_extention(self.datasetClient, output, 'bed'), self.bed)
        download_dataset(self.datasetClient, find_dataset_id_by_extention(self.datasetClient, output, 'bai'), self.bamBase + '.bai')
        download_dataset(self.datasetClient, find_dataset_id_by_extention(self.datasetClient, output, 'bam1'), self.bamBase + '.bam')

################################################################################
#main
def main():
    options = opt_validate(prepare_optparser())

    galaxyRunner = GalaxyRunner(options)
    try:
        galaxyRunner.run()
        galaxyRunner.clear()
    except:
        galaxyRunner.clear()

def test_library():
    options = opt_validate(prepare_optparser())

    galaxyRunner = GalaxyRunner(options)
    galaxyRunner.library_run()

def test_input():
    options = opt_validate(prepare_optparser())


if __name__ == '__main__':
    main()
    #test_library()
    #test_input()
