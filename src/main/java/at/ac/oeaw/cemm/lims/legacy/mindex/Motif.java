package at.ac.oeaw.cemm.lims.legacy.mindex;

/**
 *
 * @author hmuller
 */
public interface Motif {
    
    public boolean match(String motif);
    public boolean matchDirect(String motif);
    public boolean matchReverseComplement(String motif);  
    public boolean matchSequence(String sequence);
    
    public String getName();
    public void setName(String name);
    public int getMotifLength();    
    
    

}
