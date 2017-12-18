/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.validator.dto.request_form;

import at.ac.oeaw.cemm.lims.api.dto.request_form.RequestFormDTO;
import at.ac.oeaw.cemm.lims.api.dto.request_form.RequestLibraryDTO;
import at.ac.oeaw.cemm.lims.api.dto.request_form.RequestSampleDTO;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;

/**
 *
 * @author dbarreca
 */
public class RequestNormalizer {

    public static void normalizeLibraries(List<RequestLibraryDTO> libraries) {

        Set<String> libraryNames = new HashSet<>();
        
        for(RequestLibraryDTO library: libraries) {
           
            String libraryName = library.getName();
            if (libraryNames.contains(libraryName)) {
                Integer sampleNonce = 1;
                Matcher matcher = RequestFormDTO.NAME_PATTERN.matcher(libraryName);
                String prefix = matcher.matches() ? matcher.group(1) : libraryName;

                while (libraryNames.contains(libraryName)) {
                    libraryName = prefix + RequestFormDTO.DEFAULT_SUFFIX + sampleNonce;
                    sampleNonce += 1;
                }
            }

            libraryNames.add(libraryName);
            library.setName(libraryName);
        }                
    }
    
    public static void normalizeSamples(List<RequestSampleDTO> samples) {
        Set<String> sampleNames = new HashSet<>();
        
        for(RequestSampleDTO sample: samples){

            String sampleName = sample.getName();
            if (sampleName!= null && !sampleName.isEmpty()){
                if (sampleNames.contains(sampleName)) {
                    Integer sampleNonce = 1;
                    Matcher matcher = RequestFormDTO.NAME_PATTERN.matcher(sampleName);
                    String prefix = matcher.matches() ? matcher.group(1) : sampleName;

                    while (sampleNames.contains(sampleName)) {
                        sampleName = prefix + RequestFormDTO.DEFAULT_SUFFIX + sampleNonce;
                        sampleNonce += 1;
                        sample.setName(sampleName);
                    }

                }
                sampleNames.add(sampleName);
            }           


            
            String libraryName = sample.getLibrary();
            if (libraryName == null || libraryName.isEmpty()) {
                sample.setLibrary(RequestFormDTO.DEFAULT_LIBRARY);
            }

            String i5Index = sample.getI5Index();
            if (i5Index == null || i5Index.isEmpty()) {
                sample.setI5Index(RequestFormDTO.DEFAULT_INDEX);
            }

            String i7Index = sample.getI7Index();
            if (i7Index == null || i7Index.isEmpty()) {
                sample.setI7Index(RequestFormDTO.DEFAULT_INDEX);
            }

            String primerIndex = sample.getPrimerSequence();
            if (primerIndex == null || primerIndex.isEmpty()) {
                sample.setPrimerSequence(RequestFormDTO.DEFAULT_INDEX);
            }
        }
    }
    
}
