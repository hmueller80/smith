/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.analysis.impl;

import at.ac.oeaw.cemm.lims.util.Preferences;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author dbarreca
 */
public class DemuxAnalysisScript {
    private static final String LANG = "#!/bin/bash\n\n";
    private static final String CHANGE_DIR = "cd ";
    private static final String BASE_COMMAND = "bsf_submit_irf_processor.py";
    private static final String IRF_OPT = " --irf=";
    private static final String MISEQ_OPT = " --mode=miseq";
    
    private List<String> commands = new LinkedList<>();

    public DemuxAnalysisScript(String runFolder, boolean isMiSeq) {
        if (runFolder!=null && !runFolder.isEmpty()){
            commands.add(CHANGE_DIR +Preferences.getSampleSheetFolder());
            
            String demuxCommand = BASE_COMMAND;
            demuxCommand +=IRF_OPT+runFolder;
            if (isMiSeq){
                demuxCommand+=MISEQ_OPT;
            }
            
            commands.add(demuxCommand);
        }
    }
  
    public void writeToFile(String path) throws Exception {
        File scriptFile = new File(path);
        if (!scriptFile.exists()) {
            scriptFile.setExecutable(true, false);
            scriptFile.setReadable(true, false);
            scriptFile.setWritable(true, false);
            FileOutputStream fos = new FileOutputStream(scriptFile);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
            bw.write(LANG);
            bw.newLine();
            for (String command:commands) {
                bw.write(command);
                bw.newLine();
            }
            bw.close();
        }
    }

    
}
