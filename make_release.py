import shutil

src = "C:\Program Files (x86)\Fractal Softworks\Starsector\mods\Fractal Insurgency"
dest = "C:\Program Files (x86)\Fractal Softworks\Starsector\mods\Fractal Insurgency\\temp\Fractal Insurgency"
toIgnore = shutil.ignore_patterns('*.gitignore', '.idea', '.run', 'out', 'src', '*.iml','*.py','*.md')
print("\nDid you update the version number in BOTH the mod_info and the .version?\n")
print("Copying to temp directory...")
shutil.copytree(src, dest, ignore = toIgnore)
print("Creating .zip...")
shutil.make_archive('Fractal_Insurgency_v###', 'zip', dest)
print("Deleting temp directory...")
shutil.rmtree(dest)