/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.handle;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.dspace.core.Context;
import org.dspace.orm.dao.api.IHandleDao;
import org.dspace.orm.dao.api.IMetadataDao;
import org.dspace.search.DSIndexer;
import org.dspace.utils.DSpace;
import org.dspace.browse.IndexBrowse;

/**
 * A script to update the handle values in the database. This is typically used
 * when moving from a test machine (handle = 123456789) to a production service.
 *
 * @author Stuart Lewis
 */
public class UpdateHandlePrefix
{
    public static void main(String[] args) throws Exception
    {
        // There should be two paramters
        if (args.length < 2)
        {
            System.out.println("\nUsage: update-handle-prefix <old handle> <new handle>\n");
        }
        else
        {
        	DSpace dspace = new DSpace();
            // Confirm with the user that this is what they want to do
            String oldH = args[0];
            String newH = args[1];

            BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
            Context context = dspace.getContextService().getContext();
            IHandleDao handleDao = dspace.getSingletonService(IHandleDao.class);
            System.out.println("If you continue, all handles in your repository with prefix " +
                                oldH + " will be updated to have handle prefix " + newH + "\n");
            
            
            long count = handleDao.countByPrefix(oldH);
            
            System.out.println(count + " items will be updated.\n");
            System.out.print("Have you taken a backup, and are you ready to continue? [y/n]: ");
            String choiceString = input.readLine();

            if (choiceString.equalsIgnoreCase("y"))
            {
                // Make the changes
                System.out.print("Updating handle table... ");
                long updated = handleDao.updatePrefix(oldH, newH);
                System.out.println(updated + " items updated");

                System.out.print("Updating metadatavalues table... ");
                IMetadataDao metadataDao = dspace.getSingletonService(IMetadataDao.class);
                updated = metadataDao.updatePrefix(oldH, newH);
                System.out.println(updated + " metadata values updated");

                // Commit the changes
                context.complete();

                System.out.print("Re-creating browse and search indexes... ");                

                // Reinitialise the browse system
                IndexBrowse.main(new String[] {"-i"});

                // Reinitialise the browse system
                try
                {
                    DSIndexer.main(new String[0]);
                }
                catch (Exception e)
                {
                    // Not a lot we can do
                    System.out.println("Error re-indexing:");
                    e.printStackTrace();
                    System.out.println("\nPlease manually run [dspace]/bin/index-all");
                }

                // All done
                System.out.println("\nHandles successfully updated.");
            }
            else
            {
                System.out.println("No changes have been made to your data.");
            }
        }
    }
}