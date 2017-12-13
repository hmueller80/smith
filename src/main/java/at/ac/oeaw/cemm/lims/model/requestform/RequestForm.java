/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.requestform;

import at.ac.oeaw.cemm.lims.model.validator.ValidatorMessage;
import at.ac.oeaw.cemm.lims.model.validator.ValidatorSeverity;
import at.ac.oeaw.cemm.lims.util.Levenshtein;
import at.ac.oeaw.cemm.lims.util.NameFilter;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author dbarreca
 */
public class RequestForm {
    private final static String DEFAULT_NAME = "DEFAULT_NAME";
    private final static String DEFAULT_SUFFIX = "_LIMS";
    private final static Pattern NAME_PATTERN = Pattern.compile("(.*)"+DEFAULT_SUFFIX+"[0-9]+");
    private final static String DEFAULT_LIBRARY = "DEFAULT_LIB";
    private final static String DEFAULT_INDEX = "NONE";
    private final static String INDEX_REGEXP = "[ATGC]+|"+DEFAULT_INDEX;

    private Requestor requestor;    
    private Date date;
    private Map<String, Library> libraries;

    public RequestForm(Requestor requestor) {
        this.requestor = requestor;
        libraries = new LinkedHashMap<>();
        date = new Date();
    }
    
    public List<Library> getLibraries() {
        return new LinkedList<>(libraries.values());
    }
    
    public void addLibrary(Library library) {
        libraries.put(library.getName(), library);
    }

    public Requestor getRequestor() {
        return requestor;
    }

    public Set<ValidatorMessage> addAllSamples(List<RequestedSample> receivedSamples){
        for (Library library: libraries.values()) {
            library.resetSamples();
        }

        Set<ValidatorMessage> messages = new HashSet<>();
        Set<String> sampleNames = new HashSet<>();
        
        for (RequestedSample sample : receivedSamples) {
            
            String sampleName = sample.getSampleName();
            if ( sampleName == null || sampleName.trim().isEmpty()) {
                //messages.add(new ValidatorMessage(ValidatorSeverity.WARNING,"Sample Name", "Sample Name cannot be empty"));
                sampleName = DEFAULT_NAME;
            } else {
                sampleName = NameFilter.legalize(sampleName);                               
            }
            
            if (sampleNames.contains(sampleName)) {
                //messages.add(new ValidatorMessage(ValidatorSeverity.WARNING, "Sample Name", "Sample Name " + sampleName + " is duplicate"));
                Integer sampleNonce = 1;
                Matcher matcher = NAME_PATTERN.matcher(sampleName);
                String prefix = matcher.matches() ? matcher.group(1) : sampleName;
                        
                while (sampleNames.contains(sampleName)) {                   
                    sampleName = prefix+DEFAULT_SUFFIX + sampleNonce;
                    sampleNonce +=1;
                }
            }
                       
            sampleNames.add(sampleName);
            sample.setSampleName(sampleName);

            String libraryName = sample.getLibrary();
            if (libraryName == null || libraryName.trim().isEmpty()) {
                //messages.add(new ValidatorMessage(ValidatorSeverity.WARNING, "Library Name", "Library is empty for sample "+sampleName));
                libraryName = DEFAULT_LIBRARY;
            }else{
                libraryName = NameFilter.legalize(libraryName.trim().toUpperCase());
            }
            sample.setLibrary(libraryName);
            
            String i5Index = sample.getI5Index();
            i5Index = (i5Index==null || i5Index.trim().isEmpty()) ? DEFAULT_INDEX : i5Index.trim().toUpperCase();
            if(!i5Index.matches(INDEX_REGEXP)) {
                messages.add(new ValidatorMessage(ValidatorSeverity.FAIL, "I5 Index", "I5 Index in sample "+sampleName+" is not valid"));
                i5Index=DEFAULT_INDEX;
            }
            sample.setI5Index(i5Index);

            String i7Index = sample.getI7Index();
            i7Index = (i7Index==null || i7Index.trim().isEmpty()) ? DEFAULT_INDEX : i7Index.trim().toUpperCase();
            if(!i7Index.matches(INDEX_REGEXP)) {
                messages.add(new ValidatorMessage(ValidatorSeverity.FAIL, "I7 Index", "I7 Index in sample "+sampleName+" is not valid"));
                i7Index=DEFAULT_INDEX;
            }
            sample.setI7Index(i7Index);

            String primerIndex = sample.getPrimerSequence();
            primerIndex = (primerIndex==null || primerIndex.trim().isEmpty()) ? DEFAULT_INDEX : primerIndex.trim().toUpperCase();
            if(!primerIndex.matches(INDEX_REGEXP)) {
                messages.add(new ValidatorMessage(ValidatorSeverity.FAIL, "Primer sequence", "Primer sequence in sample "+sampleName+" is not valid"));
                primerIndex=DEFAULT_INDEX;
            }
            sample.setPrimerSequence(primerIndex);
            
            Library library = libraries.get(sample.getLibrary());

            if (library == null) {
                library = new Library();
                library.setName(sample.getLibrary());
                addLibrary(library);
            }

            
            for (RequestedSample otherSample : library.getSamples()) {
                RequestedSample thisSample = sample;
              
                if (DEFAULT_INDEX.equals(otherSample.getI7Index()) && DEFAULT_INDEX.equals(otherSample.getI5Index())||
                        DEFAULT_INDEX.equals(thisSample.getI7Index()) && DEFAULT_INDEX.equals(thisSample.getI5Index())) {
                    String failMessage
                            = "There is more than one sample"
                            + " in library " + library.getName()
                            + " with empty index ";

                    messages.add(new ValidatorMessage(ValidatorSeverity.FAIL, "Index collision", failMessage));
                    continue;
                }
                
                if (sample.getSampleName().compareTo(otherSample.getSampleName()) > 0) {
                    RequestedSample temp = otherSample;
                    otherSample = thisSample;
                    thisSample = temp;
                }
                String thisIndex = thisSample.getI7Index() + "_" + thisSample.getI5Index();

                String otherIndex = otherSample.getI7Index() + "_" + otherSample.getI5Index();
                Integer distance = Levenshtein.computeLevenshteinDistance(thisIndex, otherIndex);
                if (distance == 0) {
                    String failMessage
                            = "Indexes in samples " + thisSample.getSampleName()
                            + " and " + otherSample.getSampleName()
                            + " in library " + library.getName()
                            + " are equal! ";

                    messages.add(new ValidatorMessage(ValidatorSeverity.FAIL, "Index collision", failMessage));
                } else if (distance <= 2) {
                    String warnMessage
                            = "Indexes " + thisIndex
                            + " and " + otherIndex
                            + " in samples " + thisSample.getSampleName()
                            + " and " + otherSample.getSampleName()
                            + " in library " + library.getName()
                            + " have edit distance of " + distance;

                    messages.add(new ValidatorMessage(ValidatorSeverity.WARNING, "Index similarity", warnMessage));
                }
            }

            library.addSample(sample);
        }
        
        Iterator<Entry<String,Library>> iterator = libraries.entrySet().iterator();
        while(iterator.hasNext()){
            Entry<String,Library> entry = iterator.next();
            if (entry.getValue().getSamples().isEmpty()) {
                iterator.remove();
            }
       }

        return messages;
    }
     
    
}
