Checker Plugin Project README

Included below are development instructions for the checker plugin. For
general user instructions, see the homepage at:
http://types.cs.washington.edu/checker-framework/eclipse

==== Running the plugin ====

Prerequisites: checkers.jar, checkers-quals.jar, jsr308-all.jar, javac.jar
These are all available from the checker-framework and jsr308 projects
respectively. For instructions on building these, see the documentation for
the framework and jsr308.

You must put the above libraries in the lib folder (create one if missing)
in the checker-plugin folder.

To run the plugin within an Eclipse instance, open the checker-plugin project
at /checker-plugin (from the root of the repository) in Eclipse. Click on the
plugin.xml and use the "run" action in Eclipse, which should open a dialog
where you can select to "Run Eclipse Application".

==== Packaging the feature ====

To build the Eclipse feature after making changes to the source code, you
just need to open the update-site project in Eclipse. Open site.xml to
edit the update-site data. Remove the currently registered feature and click
the add button to add the current version (only required if the version
was updated). Then click the "build all" button to have Eclipse build
the jar files for the update-site.

This will create a working update site. You can upload this to a web server
to let users install the plugin from Eclipse's plugin installation process.

==== For UW's website ====

The checker plugin is uploaded to folder named:
  /cse/www2/types/checker-framework/checker-plugin/update-site-foo
where foo is a descriptive string (the current date works)

Update the symbolic link in the checker-plugin folder on the server to link
  update-site -> update-site-foo

This completes the process needed to upload a new update site in general.

If the checker framework/JSR308 jars were updated, upload those to the
lib folder in the checker-plugin folder mentioned above.

The checker-plugin folder on the server and its descendant folders/files should
be set to permissions 775.
