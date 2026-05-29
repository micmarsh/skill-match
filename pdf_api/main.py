
from fastapi import FastAPI, Response
from pydantic import BaseModel
from weasyprint import HTML
from typing import List

app = FastAPI()

def weasyprint_render(html: str):
    return HTML(string=html).render().write_pdf()

class HtmlRender(BaseModel):
    body: str

#TODO maybe dynamic filename based on hash of content or something or other?
headers = {"Content-Disposition": "inline; filename=Rendered.pdf"}

@app.post("/render_raw")
async def render_pdf(html: HtmlRender):
    pdf_bytes = weasyprint_render(html.body)
    return Response(pdf_bytes, media_type="application/pdf", headers=headers)

class ResumeRender(BaseModel):
    skills: List[str]

# TODO more robust, command-line arg/dependency injection(?)-based thing!
template:str
with open("/home/michael/Documents/JobSearch/Resume_Template.html") as f:
    template = f.read()

@app.post("render_resume")
async def render_resume(input: ResumeRender):
    


# TODO probably delete these, just for playing around
@app.get("/")
async def read_root():
    return {"message": "Hello, Dawg!"}

@app.get("/square")
async def calculate_square(num: int):
    return {"numba": num, "square": num ** 2}
