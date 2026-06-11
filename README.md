# skill-match

An extremely specialized (to me) resume generator, for generating custom "Skills" sections of my resume.

Re-Frame Frontend with a Python (for access to `weasyprint`) backend.

## Running Non-Dev (JS Optimized, No Hot Reloads)

(assuming you've installed all the npm and python packages/venv you need already)

Make sure you've run `npx shadow-cljs release :app` recently

Run `cd pdf_api/ && venv/bin/uvicorn main:app` (you may need to substitue `venv` for `.venv` depending on what you actually set up)

Navigate to http://localhost:8000

